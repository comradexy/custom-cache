package cn.comradexy.demo.separation.dbrouter;

import cn.comradexy.demo.mapper.ServeAccessMapper;
import cn.comradexy.demo.mapper.ServeArchiveMapper;
import cn.comradexy.demo.mapper.ServeMapper;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据源配置
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-02
 * @Description: 数据源配置
 */
@Configuration
public class DynamicDataSourceConfig implements EnvironmentAware {
    public static final String HOT_DATA_SOURCE = "hot";
    public static final String COLD_DATA_SOURCE = "cold";
    public static final Map<Object, Object> TARGET_DATA_SOURCES = new HashMap<>();
    public static final Map<String, SqlSessionFactory> SQL_SESSION_FACTORIES = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public DataSource dataSource() {
        // 设置数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(TARGET_DATA_SOURCES);
        dynamicDataSource.setDefaultTargetDataSource(TARGET_DATA_SOURCES.get(HOT_DATA_SOURCE));

        return dynamicDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Throwable {
        UserTransactionManager atomikosTransactionManager = new UserTransactionManager();
        atomikosTransactionManager.init();
        UserTransaction userTransaction = new UserTransactionImp();

        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        // 创建数据源
        DataSource hotDataSource = createAtomikosDataSource(HOT_DATA_SOURCE, environment);
        DataSource coldDataSource = createAtomikosDataSource(COLD_DATA_SOURCE, environment);

        // 添加到数据源集合
        TARGET_DATA_SOURCES.put(HOT_DATA_SOURCE, hotDataSource);
        TARGET_DATA_SOURCES.put(COLD_DATA_SOURCE, coldDataSource);

        // 创建 SqlSessionFactory
        try {
            SQL_SESSION_FACTORIES.put(HOT_DATA_SOURCE, getHotSqlSessionFactory(hotDataSource));
            SQL_SESSION_FACTORIES.put(COLD_DATA_SOURCE, getColdSqlSessionFactory(coldDataSource));
        } catch (Exception e) {
            logger.error("创建 SqlSessionFactory 失败", e);
        }
    }

    /**
     * 创建 Atomikos 数据源
     *
     * @param dataSourceName 数据源名称(唯一)
     * @param environment    环境
     */
    private DataSource createAtomikosDataSource(String dataSourceName, Environment environment) {
        // 获取数据源配置
        String prefix = "router.jdbc.datasource." + dataSourceName + ".";
        String url = environment.getProperty(prefix + "url");
        String username = environment.getProperty(prefix + "username");
        String password = environment.getProperty(prefix + "password");

        Properties properties = new Properties();
        properties.setProperty("url", url);
        properties.setProperty("user", username);
        properties.setProperty("password", password);

        // 创建 Atomikos 数据源
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setUniqueResourceName(dataSourceName);
        xaDataSource.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource"); // XA 兼容的 DataSource
        xaDataSource.setXaProperties(properties);
        xaDataSource.setPoolSize(5);
        return xaDataSource;
    }

    private SqlSessionFactory getHotSqlSessionFactory(DataSource hotDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(hotDataSource);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.addMapper(ServeMapper.class);
        configuration.addMapper(ServeArchiveMapper.class);
        configuration.addMapper(ServeAccessMapper.class);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }

    private SqlSessionFactory getColdSqlSessionFactory(DataSource coldDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(coldDataSource);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.addMapper(ServeMapper.class);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }
}
