package cn.comradexy.demo.separation;

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
public class SeparationAspect {
    private final Logger logger = LoggerFactory.getLogger(SeparationAspect.class);

    @Pointcut("@annotation(cn.comradexy.demo.annotation.HotColdSeparation)")
    public void hotColdSeparation() {
    }

    @Around("hotColdSeparation()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        logger.info("hotColdSeparation");
        try {
            // 冷热识别
//            if (热) {
//                DataSourceContextHolder.setDataSourceType("hot");
//            } else {
//                DataSourceContextHolder.setDataSourceType("cold");
//            }
            return jp.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }

}
