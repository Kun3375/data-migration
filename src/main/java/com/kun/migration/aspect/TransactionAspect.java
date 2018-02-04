package com.kun.migration.aspect;

import com.kun.migration.util.CountUtil;
import com.kun.migration.util.JdbcUtil;
import com.kun.migration.util.CountUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 15:36
 */
@Aspect
@Component
public class TransactionAspect {
    
    private static final Logger log = LoggerFactory.getLogger(TransactionAspect.class);
    @Autowired
    private JdbcUtil jdbcUtil;
    
    @Pointcut("execution(public * com.kun.migration.controller.MigrationController.*(..))")
    public void controllerCut() {}
    
    @Around("@annotation(dataSource)")
    public Object transaction(ProceedingJoinPoint pjp, DataSource dataSource) {
        // 开启连接，开始事务
        jdbcUtil.start(dataSource.value());
        try {
            Object forReturn = pjp.proceed();
            jdbcUtil.commit();
            CountUtil.release();
            return forReturn;
        } catch (Throwable throwable) {
            jdbcUtil.rollBack();
            log.info("发生异常，回滚本次！", throwable);
            CountUtil.release();
        }
        return 0;
    }
    
    @AfterReturning("controllerCut()")
    public void clearCache() {
        jdbcUtil.clearCache();
    }
    
    @Before("@annotation(dataSourceSwitch)")
    public void dataSourceSwitch(DataSourceSwitch dataSourceSwitch) {
        jdbcUtil.switchConnection(dataSourceSwitch.value());
    }
    
}
