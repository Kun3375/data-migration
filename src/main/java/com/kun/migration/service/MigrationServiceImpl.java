package com.kun.migration.service;

import com.kun.migration.service.manager.MigrationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/21 19:16
 */
@Service
public class MigrationServiceImpl implements MigrationService {
    
    @Autowired
    private MigrationManager migrationManager;
    
    @Override
    @Async
    public void asyncMigration(int index, int size) {
        asyncMigration(index, size, false);
    }
    
    @Override
    @Async
    public void asyncMigration(int index, int size, boolean multi) {
        if (multi) {
            migrationManager.multiMigration(index, size);
            return;
        }
        migrationManager.migration(index, size);
    }
}
