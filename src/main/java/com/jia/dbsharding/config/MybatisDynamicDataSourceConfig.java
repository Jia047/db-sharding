package com.jia.dbsharding.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * mybatis动态切换数据源
 */
@Configuration
public class MybatisDynamicDataSourceConfig {

    private final DataSource masterDB;
    private final DataSource slaveDB;

    @Autowired
    public MybatisDynamicDataSourceConfig(@Qualifier(DataSources.MASTER_DB) DataSource masterDB
            , @Qualifier(DataSources.SLAVE_DB) DataSource slaveDB) {
        this.masterDB = masterDB;
        this.slaveDB = slaveDB;
    }

    /**
     * 返回一个名为 dynamicDataSource 的动态数据源
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDB);

        Map<Object, Object> dsMap = new HashMap<Object, Object>();

        dsMap.put(DataSources.MASTER_DB, masterDB);
        dsMap.put(DataSources.SLAVE_DB, slaveDB);
        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean(){
        SqlSessionFactoryBean sqlSessionFactoryBeans = new SqlSessionFactoryBean();
        // 配置数据源，如果没有 dynamicDataSource作为数据源将不能实现切换
        sqlSessionFactoryBeans.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBeans;
    }
}
