package com.kun.migration.configuration;

import com.kun.migration.configuration.properties.DataProperties;
import com.kun.migration.configuration.properties.JdbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 12:53
 */
@Configuration
@EnableConfigurationProperties({JdbcProperties.class, DataProperties.class})
public class SpringWebConfig {
    
}
