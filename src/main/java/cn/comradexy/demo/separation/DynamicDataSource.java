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
        // TODO: 动态数据源
        return null;
    }
}
