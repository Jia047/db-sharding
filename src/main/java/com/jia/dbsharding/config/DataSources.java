package com.jia.dbsharding.config;

public interface DataSources {

    /**
     * 存储数据源
     */
    String MASTER_DB = "masterDB";

    /**
     * 只读数据源
     */
    String SLAVE_DB = "slaveDB";
}
