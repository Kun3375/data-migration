package com.kun.migration.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/9 15:57
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    
    /**
     * dataSource bean name
     * @return
     */
    String[] value() default "firstDataSource";
    
}
