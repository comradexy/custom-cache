package cn.comradexy.demo.separation;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-02
 * @Description: 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        // 通过ThreadLocal获取当前线程的数据源类型
        return DataSourceContextHolder.getDataSourceType();
    }
}
