package cn.comradexy.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * demo config
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-29
 * @Description: demo config
 */
@Configuration
@MapperScan("cn.comradexy.demo.mapper")
public class DemoConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


}
