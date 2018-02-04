package com.kun.migration.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/6 9:45
 */
@Configuration
public class DataSourceConfig {
    
    
    @Bean
    @ConfigurationProperties("datasource.first")
    public DataSource firstDataSource() {
        return new ComboPooledDataSource();
    }
    
    @Bean
    @ConfigurationProperties("datasource.second")
    public DataSource secondDataSource() {
        return new ComboPooledDataSource();
    }
    
    
}
