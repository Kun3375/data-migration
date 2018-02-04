package com.kun.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 启动类
 * @author CaoZiye
 */
@SpringBootApplication
@EnableCaching
public class DataMigrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataMigrationApplication.class, args);
	}
}
