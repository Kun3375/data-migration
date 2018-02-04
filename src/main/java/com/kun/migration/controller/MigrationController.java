package com.kun.migration.controller;

import com.kun.migration.service.MigrationService;
import com.kun.migration.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 10:30
 */
@RestController
public class MigrationController {
    
    private static final Logger log = LoggerFactory.getLogger(MigrationController.class);
    @Autowired
    private MigrationService migrationService;
    
    @GetMapping("/data_migration/{size}")
    public String migration(@PathVariable("size") int size) {
        
        int index = 0;
        do {
            if (CountUtil.isFinish()) {
                break;
            }
            migrationService.asyncMigration(index, size);
            index += size;
        } while (CountUtil.join() == 1);
        log.info("结束，成功总计：{}", CountUtil.getSuccessCount());

        return "?";
    }
    
    
    @RequestMapping("/data_migration/{size}")
    public String multiMigration(@PathVariable("size") int size) {
        
        int index = 0;
        do {
            if (CountUtil.isFinish()) {
                break;
            }
            migrationService.asyncMigration(index, size, true);
            index += size;
        } while (CountUtil.join() == 1);
        log.info("结束，成功总计：{}", CountUtil.getSuccessCount());
        return "?";
    }
    
}
