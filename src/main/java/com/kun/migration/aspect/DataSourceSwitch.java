package com.kun.migration.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/12 17:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceSwitch {
    
    /**
     * dataSource bean name
     * @return
     */
    String value() default "firstDataSource";
    
}
