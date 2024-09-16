package cn.comradexy.demo.separation;

import cn.comradexy.demo.mapper.ServeArchiveMapper;
import cn.comradexy.demo.mapper.ServeMapper;
import cn.comradexy.demo.model.domain.Serve;
import cn.comradexy.demo.model.domain.ServeArchive;
import cn.comradexy.demo.separation.dbrouter.DataSourceConfig;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import cn.comradexy.demo.service.IServeAccessService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 冷热分离切面类
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-03
 * @Description: 冷热分离切面类
 */
@Aspect
@Component
public class SeparatedAspect {
    private final Logger logger = LoggerFactory.getLogger(SeparatedAspect.class);

    @Resource
    private IServeAccessService serveAccessService;

    @Resource
    private ServeArchiveMapper serveArchiveMapper;

    @Resource
    private ServeMapper serveMapper;

    @Pointcut("@annotation(cn.comradexy.demo.separation.Separated)")
    public void separation() {
    }

    @Around("separation()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        logger.info("hotColdSeparation");
        try {
            // 获取@Separated注解的operateType
            OperateType operateType = getOperateType(jp);
            if (operateType == null) {
                logger.warn("operateType is null: {}", jp.getSignature());
                return jp.proceed();
            }

            Object result = null;

            switch (operateType) {
                case SELECT_ONE:
                    // 先查询热库
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                    result = jp.proceed();

                    // 如果查询不到，再查询冷库
                    if (result == null) {
                        DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
                        result = jp.proceed();
                        DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                    }

                    // 记录访问
                    if (null != result) {
                        serveAccessService.recordAccess(((Serve) result).getId());
                    }

                    if (result != null) {
                        logger.info("success: 查询服务--[id: {}]", ((Serve) result).getId());
                    } else {
                        logger.info("fail: 查询服务--[args: {}]", jp.getArgs());
                    }

                    break;

                case SELECT_BATCH:
                    // 并发查询热库和冷库
                    Set<Serve> batchSet = CompletableFuture.supplyAsync(() -> {
                        // 查询冷库
                        DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
                        try {
                            return new HashSet<>((List<Serve>) jp.proceed());
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }).thenCombine(CompletableFuture.supplyAsync(() -> {
                        // 查询热库
                        DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                        try {
                            return new HashSet<>((List<Serve>) jp.proceed());
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }), (coldData, hotData) -> {
                        // 合并数据，优先保留热库的数据
                        hotData.addAll(coldData);
                        return hotData;
                    }).join();

                    // 记录访问
                    batchSet.forEach((item) -> serveAccessService.recordAccess(item.getId()));

                    // 转为List返回
                    result = List.copyOf(batchSet);

                    logger.info("success: 批量查询服务--[args: {}]", jp.getArgs());

                    break;

                case SELECT_ONE_FOR_UPDATE:
                    Long id = jp.getArgs()[0] instanceof Long ? (long) jp.getArgs()[0] : null;
                    if (id == null) {
                        throw new RuntimeException("参数错误: id=" + id);
                    }

                    // 先查热库，如果存在，锁住数据后返回
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                    Serve serve = serveMapper.selectById(id);
                    if (serve != null) {
                        // 锁住数据
                        result = jp.proceed();

                        // 记录访问
                        serveAccessService.recordAccess(serve.getId());

                        break;
                    }

                    // 如果不存在，查归档表
                    ServeArchive serveArchive = serveArchiveMapper.selectById(id);
                    if (null == serveArchive || !serveArchive.getStorageType().equals("COLD")) {
                        // 如果归档表中不存在，或者归档表中存在但状态不为COLD，则报错，数据不存在
                        throw new RuntimeException("服务数据不存在: id=" + id);
                    }

                    // 从冷库中获取数据
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
                    serve = serveMapper.selectById(id);

                    // 回写到热库
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                    serveMapper.insert(serve);

                    // 更新归档表状态
                    serveArchive.setStorageType("HOT");
                    serveArchiveMapper.updateStorageType(serveArchive);

                    // 锁住数据并返回
                    result = jp.proceed();

                    // 记录访问
                    serveAccessService.recordAccess(serve.getId());

                    break;

                case SELECT_BATCH_FOR_UPDATE:
                    Long serveItemId = jp.getArgs()[0] instanceof Long ? (long) jp.getArgs()[0] : null;
                    Long regionId = jp.getArgs()[1] instanceof Long ? (long) jp.getArgs()[1] : null;
                    if (serveItemId == null || regionId == null) {
                        throw new RuntimeException("参数错误: serveItemId=" + serveItemId + ", regionId=" + regionId);
                    }

                    // 先预查询，获取冷库中存在而热库中不存在的数据
                    Set<Serve> coldOnlySet = CompletableFuture.supplyAsync(() -> {
                        // 查询冷库
                        DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
                        try {
                            return new HashSet<>(serveMapper.selectByItemAndRegion(serveItemId, regionId));
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }).thenCombine(CompletableFuture.supplyAsync(() -> {
                        // 查询热库
                        DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                        try {
                            return new HashSet<>(serveMapper.selectByItemAndRegion(serveItemId, regionId));
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }), (coldData, hotData) -> {
                        // 获取冷库中存在而热库中不存在的数据
                        coldData.removeAll(hotData);
                        return coldData;
                    }).join();

                    // 然后回写到热库
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                    coldOnlySet.forEach(serveMapper::insert);

                    // 最后在热库中条件查询并锁住数据
                    result = jp.proceed();

                    // 记录访问
                    ((List<Serve>) result).forEach((item) -> serveAccessService.recordAccess(item.getId()));

                    break;

                case COUNT:
                    // 统计热库和归档表中状态为COLD的数据量，然后汇总
                    CompletableFuture<Integer> archiveFuture = CompletableFuture.supplyAsync(() -> {
                        DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                        return serveArchiveMapper.countCold();
                    });
                    int countHot = (int) jp.proceed();
                    int countArchive = archiveFuture.join();
                    result = countHot + countArchive;

                    // 统计不需要记录访问，不算在访问次数内

                    logger.info("success: 统计服务");

                    break;

                case INSERT:
                    serve = jp.getArgs()[0] instanceof Serve ? (Serve) jp.getArgs()[0] : null;

                    // 插入前检查：根据唯一键查询热库归档记录表，归档不存在走原有插入逻辑，归档记录存在则返回记录已存在，走后续幂等逻辑
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                    if (null != serveArchiveMapper.selectById(serve.getId())) {
                        throw new RuntimeException("服务数据已存在: id=" + serve.getId());
                    }

                    // 插入热库
                    result = jp.proceed();

                    // 记录访问
                    serveAccessService.recordAccess(serve.getId());

                    // TODO
                    // 临界问题：先查询归档记录表数据不存在，然后发生归档删除热库数据，再 insert 订单，此时幂等失效，有订单重复风险
                    // 解决方案：热库数据延迟删除，在数据写入冷库并且归档表新增记录后，延迟 Xmin 才删除热库数据，更新归档状态

                    break;

                case UPDATE:
                    // TODO
                    serve = jp.getArgs()[0] instanceof Serve ? (Serve) jp.getArgs()[0] : null;
                    if (serve == null) {
                        throw new RuntimeException("参数错误: serve=" + serve);
                    }

                    // 查询归档表
                    serveArchive = serveArchiveMapper.selectById(serve.getId());
                    if(serveArchive == null) {
                        throw new RuntimeException("服务数据不存在: id=" + serve.getId());
                    }

                    // 如果归档状态为COLD
                    if(serveArchive.getStorageType().equals("COLD")) {
                        // 获取冷库数据

                        // 回写到热库

                        // 更新归档表状态为HOT

                        // 更新热库数据

                    }else{
                        if(serveArchive.getStorageType().equals("PROCESSING")) {
                            // 更新归档状态为HOT
                        }

                        // 更新热库数据

                    }

                    // 记录访问


                    break;

                case DELETE:
                    // TODO

                    break;

                default:
                    logger.warn("operateType is not supported: {}", operateType);
                    result = jp.proceed();

            }

            return result;
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }

    private OperateType getOperateType(ProceedingJoinPoint jp) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Separated separated = method.getAnnotation(Separated.class);
        return separated == null ? null : separated.operateType();
    }
}
