package cn.comradexy.demo.separation;

import cn.comradexy.demo.separation.dbrouter.DataSourceConfig;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 冷热分离切面类
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-03
 * @Description: 冷热分离切面类
 */
@Aspect
@Component
public class SeparationJoinPoint {
    private final Logger logger = LoggerFactory.getLogger(SeparationJoinPoint.class);

    @Pointcut("@annotation(cn.comradexy.demo.separation.HotColdSeparation)")
    public void hotColdSeparation() {
    }

    @Around("hotColdSeparation()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        logger.info("hotColdSeparation");
        try {
            // TODO
            DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
            return jp.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }

}
