package cn.comradexy.demo.config;

import cn.comradexy.demo.separation.DynamicDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-02
 * @Description: 数据源配置
 */
@Configuration
public class DataSourceConfig implements EnvironmentAware {
    public static final String HOT_DATA_SOURCE = "hot";
    public static final String COLD_DATA_SOURCE = "cold";

    private final Map<Object, Object> targetDataSources = new HashMap<>();

    @Bean
    public DataSource dataSource() {
        // 设置数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);

        return dynamicDataSource;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        // 创建数据源
        DataSource hotDataSource = createDataSource(HOT_DATA_SOURCE, environment);
        DataSource coldDataSource = createDataSource(COLD_DATA_SOURCE, environment);

        // 添加到数据源集合
        targetDataSources.put(HOT_DATA_SOURCE, hotDataSource);
        targetDataSources.put(COLD_DATA_SOURCE, coldDataSource);
    }

    private DataSource createDataSource(String dataSourceName, Environment environment) {
        // 获取数据源配置
        String prefix = "router.jdbc.datasource." + dataSourceName + ".";
        String url = environment.getProperty(prefix + "url");
        String username = environment.getProperty(prefix + "username");
        String password = environment.getProperty(prefix + "password");
        String driverClassName = environment.getProperty(prefix + "driver-class-name");

        // 创建数据源
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

}
