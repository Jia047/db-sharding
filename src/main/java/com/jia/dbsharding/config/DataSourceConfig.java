package com.jia.dbsharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 配置数据源
 */
@Configuration
public class DataSourceConfig {

    /**
     * @ConfigurationProperties 通过不同的前缀来配置不同的数据源
     * destroyMethod = "close" 当数据库连接不使用的时候,就把该连接重新放到数据池中,方便下次使用调用.
     */
    @Bean(destroyMethod = "close", name = DataSources.MASTER_DB)
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(destroyMethod = "close", name = DataSources.SLAVE_DB)
    @ConfigurationProperties(prefix = "spring.datasourceslave")
    public DataSource dataSourceSlave(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }
}
