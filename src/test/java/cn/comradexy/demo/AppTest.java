package cn.comradexy.demo;

import cn.comradexy.demo.mapper.ServeAccessMapper;
import cn.comradexy.demo.mapper.ServeArchiveMapper;
import cn.comradexy.demo.mapper.ServeMapper;
import cn.comradexy.demo.model.domain.Serve;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import cn.comradexy.demo.separation.dbrouter.DynamicDataSourceConfig;
import cn.comradexy.demo.utils.RabbitMQUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class AppTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ServeMapper serveMapper;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private RabbitMQUtils rabbitMQUtils;

    @Test
    public void delayQueueTest() {
        System.out.println(LocalDateTime.now() + ": Sending delayed message...");
        rabbitMQUtils.sendDelayedMessage("Hello, RabbitMQ!", 5000);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void dbRouterTest() {
        DataSourceContextHolder.setDataSourceType(DynamicDataSourceConfig.HOT_DATA_SOURCE);
        System.out.println(serveMapper.count());
        DataSourceContextHolder.setDataSourceType(DynamicDataSourceConfig.COLD_DATA_SOURCE);
        System.out.println(serveMapper.count());
    }

    @Test
    public void atomikosTest() {
        transactionTemplate.executeWithoutResult(status -> {
            try {
//                DataSource hotDataSource = getHotDataSource();
//                DataSource coldDataSource = getColdDataSource();
//                JdbcTemplate hotJdbcTemplate = new JdbcTemplate(hotDataSource);
//                JdbcTemplate coldJdbcTemplate = new JdbcTemplate(coldDataSource);

                try (SqlSession hotSqlSession = DynamicDataSourceConfig.SQL_SESSION_FACTORIES
                        .get(DynamicDataSourceConfig.HOT_DATA_SOURCE).openSession();
                     SqlSession coldSqlSession = DynamicDataSourceConfig.SQL_SESSION_FACTORIES
                             .get(DynamicDataSourceConfig.COLD_DATA_SOURCE).openSession()) {
                    ServeArchiveMapper hotServeArchiveMapper = hotSqlSession.getMapper(ServeArchiveMapper.class);
                    System.out.println(hotServeArchiveMapper.selectById(100001L));

                    ServeMapper hotServeMapper = hotSqlSession.getMapper(ServeMapper.class);
                    Serve serve = hotServeMapper.selectById(1L);
                    serve.setServeItemId(111L);
                    hotServeMapper.update(serve);

                    ServeMapper coldServeMapper = coldSqlSession.getMapper(ServeMapper.class);
                    coldServeMapper.insert(serve);
                }

                throw new Exception();
            } catch (Exception e) {
                status.setRollbackOnly();
            }
        });
    }

}
