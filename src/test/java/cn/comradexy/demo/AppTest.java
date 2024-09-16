package cn.comradexy.demo;

import cn.comradexy.demo.separation.dbrouter.DataSourceConfig;
import cn.comradexy.demo.mapper.ServeMapper;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Random;
import java.util.Scanner;
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

    @Test
    void switchTest() {
        Object result = null;
        int n = 2;
        switch (n) {
            case 1:
                int a = 1;
                result = 1;
                break;
            case 2:
                a = 2;
                System.out.println(a);
                result = 2;
                break;
            default:
                result = 3;
        }

        System.out.println(result);
    }

}
