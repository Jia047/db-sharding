package com.jia.dbsharding.config;

/**
 * 切换数据源
 */
public class DataSourceHolder {
    /**
     * 默认数据源
     */
    public static final String DEFALUT_DATASOURCE = DataSources.MASTER_DB;
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    // 设置数据源名
    public static void setDB(String datasource){
        // 日志记录 切换到 + datasource + 数据源
        contextHolder.set(datasource);
    }

    // 获取数据源名
    public static String getDB(){
        return contextHolder.get();
    }

    // 清楚数据源名
    public static void cleanDB(){
        contextHolder.remove();
    }
}
