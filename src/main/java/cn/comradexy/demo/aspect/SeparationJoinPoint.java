package cn.comradexy.demo.aspect;

import cn.comradexy.demo.dbrouter.DataSourceContextHolder;
import cn.comradexy.demo.mapper.UserMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    @Resource
    UserMapper userMapper;

    @Pointcut("@annotation(cn.comradexy.demo.annotation.HotColdSeparation)")
    public void hotColdSeparation() {
    }

    @Around("hotColdSeparation()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        logger.info("hotColdSeparation");
        try {
//            String key = (String) jp.getArgs()[0];
//            if (isHot(key)) {
//                DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
//            } else {
//                DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
//            }
            return jp.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
        }
    }

}
