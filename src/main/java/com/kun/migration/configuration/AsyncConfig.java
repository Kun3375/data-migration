package com.kun.migration.configuration;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/21 17:33
 */
@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport{
    
    @Override
    public Executor getAsyncExecutor() {
        return Executors.newFixedThreadPool(5);
    }
    
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
