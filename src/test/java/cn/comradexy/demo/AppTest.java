package cn.comradexy.demo;

import cn.comradexy.demo.mapper.ServeAccessMapper;
import cn.comradexy.demo.mapper.ServeArchiveMapper;
import cn.comradexy.demo.mapper.ServeMapper;
import cn.comradexy.demo.model.domain.Serve;
import cn.comradexy.demo.separation.dbrouter.DataSourceContextHolder;
import cn.comradexy.demo.separation.dbrouter.DynamicDataSourceConfig;
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

@SpringBootTest
class AppTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ServeMapper serveMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Test
    public void dbRouterTest() {
        DataSourceContextHolder.setDataSourceType(DynamicDataSourceConfig.HOT_DATA_SOURCE);
        System.out.println(serveMapper.count());
        DataSourceContextHolder.setDataSourceType(DynamicDataSourceConfig.COLD_DATA_SOURCE);
        System.out.println(serveMapper.count());
    }

    @Test
    public void atomikosTest() throws Exception {
        transactionTemplate.executeWithoutResult(status -> {
            try {
//                DataSource hotDataSource = getHotDataSource();
//                DataSource coldDataSource = getColdDataSource();
//                JdbcTemplate hotJdbcTemplate = new JdbcTemplate(hotDataSource);
//                JdbcTemplate coldJdbcTemplate = new JdbcTemplate(coldDataSource);

                try (SqlSession hotSqlSession = getHotSqlSessionFactory().openSession();
                     SqlSession coldSqlSession = getColdSqlSessionFactory().openSession()) {
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

    private DataSource getHotDataSource() {
        return (DataSource) DynamicDataSourceConfig.TARGET_DATA_SOURCES.get(DynamicDataSourceConfig.HOT_DATA_SOURCE);
    }

    private DataSource getColdDataSource() {
        return (DataSource) DynamicDataSourceConfig.TARGET_DATA_SOURCES.get(DynamicDataSourceConfig.COLD_DATA_SOURCE);
    }

    private SqlSessionFactory getHotSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getHotDataSource());

        Configuration configuration = new Configuration();
        configuration.addMapper(ServeMapper.class);
        configuration.addMapper(ServeArchiveMapper.class);
        configuration.addMapper(ServeAccessMapper.class);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }

    private SqlSessionFactory getColdSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getColdDataSource());

        Configuration configuration = new Configuration();
        configuration.addMapper(ServeMapper.class);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }
}
