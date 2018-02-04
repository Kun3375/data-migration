package com.kun.migration.service.manager;

import java.util.concurrent.Future;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 15:41
 */
public interface MigrationManager {
    
    Future<Integer> migration(int index, int size);
    
    Future<Integer> multiMigration(int index, int size);
    
}
