package cn.comradexy.demo;

import cn.comradexy.demo.separation.dbrouter.DataSourceConfig;
import cn.comradexy.demo.mapper.ServeMapper;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AppTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ServeMapper serveMapper;

    @Test
    void dbRouterTest() {
        DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
        int hot_count = serveMapper.count();
        logger.info("hot_count: {}", hot_count);

        DataSourceContextHolder.setDataSourceType(DataSourceConfig.COLD_DATA_SOURCE);
        int cold_count = serveMapper.count();
        logger.info("cold_count: {}", cold_count);
    }

}
