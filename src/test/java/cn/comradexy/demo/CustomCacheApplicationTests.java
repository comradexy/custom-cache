package cn.comradexy.demo;

import cn.comradexy.demo.mapper.UserMapper;
import cn.comradexy.demo.separation.DataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Statement;

@SpringBootTest
class CustomCacheApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    DataSource dataSource;

    @Resource
    UserMapper userMapper;

    @Test
    void dbRouterTest() {
        DataSourceContextHolder.setDataSourceType("hot");
        int hot_count = userMapper.count();
        logger.info("hot_count: {}", hot_count);

        DataSourceContextHolder.setDataSourceType("cold");
        int cold_count = userMapper.count();
        logger.info("cold_count: {}", cold_count);
    }

}
