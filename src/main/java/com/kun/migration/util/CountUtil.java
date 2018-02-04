package com.kun.migration.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/21 18:09
 */
public class CountUtil {
    
    private static final Logger log = LoggerFactory.getLogger(CountUtil.class);
    private static final int THREAD_COUNT = 5;
    
    private static Semaphore semaphore = new Semaphore(THREAD_COUNT);
    private static AtomicInteger successCount = new AtomicInteger(0);
    private static AtomicBoolean finish = new AtomicBoolean(false);
    
    public static int join() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
    
    public static void release() {
        try {
            semaphore.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int getSuccessCount() {
        try {
            semaphore.acquire(THREAD_COUNT);
            semaphore.release(THREAD_COUNT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return successCount.get();
    }
    
    public static boolean isFinish() {
        boolean f = finish.get();
        if (f) {
            semaphore.release();
        }
        return f;
    }
    
    public static void end() {
        finish.set(true);
    }
    
    public static class AddCountCallback implements ListenableFutureCallback<Integer> {
        
        @Override
        public void onFailure(Throwable ex) {
        
        }
        
        @Override
        public void onSuccess(Integer result) {
            successCount.addAndGet(result);
        }
    }
    
    
}
