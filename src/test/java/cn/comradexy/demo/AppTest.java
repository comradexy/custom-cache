package cn.comradexy.demo;

import cn.comradexy.demo.separation.dbrouter.DataSourceConfig;
import cn.comradexy.demo.mapper.ServeMapper;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
class AppTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ServeMapper serveMapper;

    @Test
    void dbRouterTest() {
        DataSourceContextHolder.setDataSourceType(DataSourceConfig.HOT_DATA_SOURCE);
        System.out.println(serveMapper.count());

    }

}
