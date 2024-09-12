package cn.comradexy.demo.separation;

import cn.comradexy.demo.model.domain.Serve;
import cn.comradexy.demo.separation.dbrouter.DataSourceConfig;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import cn.comradexy.demo.service.IServeArchiveService;
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
    private IServeArchiveService serveArchiveService;

    @Pointcut("@annotation(cn.comradexy.demo.separation.Separated)")
    public void separation() {
    }

    @Around("separation()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        logger.info("hotColdSeparation");
        try {
            // TODO

            // 获取@Separated注解的operateType
            OperateType operateType = getOperateType(jp);
            Object result = null;
            if (operateType == OperateType.SELECT_ONE) {
                // 先查询热数据源
                DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                result = jp.proceed();
                // 如果查询不到，再查询冷数据源
                if (result == null) {
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
                    result = jp.proceed();
                    DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
                }
                // 更新归档数据
                Serve serve = (Serve) result;
                if (serveArchiveService.recordServeAccess(serve.getId())) {
                    logger.info("服务访问记录成功: {}", serve.getId());
                } else {
                    logger.error("服务访问记录失败: {}", serve.getId());
                }
            } else if (operateType == OperateType.INSERT) {

            } else if (operateType == OperateType.UPDATE) {

            } else if (operateType == OperateType.DELETE) {

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
