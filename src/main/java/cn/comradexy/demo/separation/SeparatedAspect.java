package cn.comradexy.demo.separation;

import cn.comradexy.demo.model.domain.Serve;
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

    @Pointcut("@annotation(cn.comradexy.demo.separation.Separated)")
    public void separation() {
    }

    @Around("separation()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        logger.info("hotColdSeparation");
        try {
            // 获取@Separated注解的operateType
            OperateType operateType = getOperateType(jp);

            // 获取方法参数
            StringBuilder args = new StringBuilder();
            for (Object arg : jp.getArgs()) {
                args.append(arg).append(",");
            }

            Object result = null;

            if (operateType == OperateType.SELECT_ONE) {
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
                    serveAccessService.recordAccess((Serve) result);
                }

                if (result != null) {
                    logger.info("success: 查询服务--[{}]", args);
                } else {
                    logger.info("fail: 查询服务--[{}]", args);
                }
            } else if (operateType == OperateType.SELECT_BATCH) {
                // 查询冷库
                DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
                Set<Serve> coldData = new HashSet<>((List<Serve>) jp.proceed());

                // 查询热库
                DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                Set<Serve> hotData = new HashSet<>((List<Serve>) jp.proceed());

                // 合并数据，优先保留热库的数据
                hotData.addAll(coldData);
                result = hotData;

                logger.info("success: 批量查询服务--[{}]", args);
            } else if (operateType == OperateType.SELECT_FOR_UPDATE) {
                // TODO

            } else if (operateType == OperateType.INSERT) {
                // TODO

            } else if (operateType == OperateType.UPDATE) {
                // TODO

            } else if (operateType == OperateType.DELETE) {
                // TODO

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
