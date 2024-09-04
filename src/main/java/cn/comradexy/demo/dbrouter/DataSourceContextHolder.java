package cn.comradexy.demo.dbrouter;

/**
 * 数据源上下文
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-09-03
 * @Description: 数据源上下文
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
