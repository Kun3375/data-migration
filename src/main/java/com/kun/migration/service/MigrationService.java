package com.kun.migration.service;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/21 19:15
 */
public interface MigrationService {
    
    void asyncMigration(int index, int size);
    
    void asyncMigration(int index, int size, boolean multi);
    
}
