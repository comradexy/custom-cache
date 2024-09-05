package cn.comradexy.demo;

import cn.comradexy.demo.config.DataSourceConfig;
import cn.comradexy.demo.mapper.UserMapper;
import cn.comradexy.demo.dbrouter.DataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootTest
class CustomCacheApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UserMapper userMapper;

    @Test
    void dbRouterTest() {
        DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
        int hot_count = userMapper.count();
        logger.info("hot_count: {}", hot_count);

        DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
        int cold_count = userMapper.count();
        logger.info("cold_count: {}", cold_count);
    }

}
