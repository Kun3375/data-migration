package com.kun.migration.service.manager;

import com.kun.migration.aspect.DataSource;
import com.kun.migration.configuration.properties.DataProperties;
import com.kun.migration.dao.MigrationRepository;
import com.kun.migration.entity.Relationship;
import com.kun.migration.util.CountUtil;
import com.kun.migration.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 11:22
 */
@Service
public class MigrationManagerImpl implements MigrationManager {
    
    private static final Logger log = LoggerFactory.getLogger(MigrationManagerImpl.class);
    
    @Autowired
    private DataProperties dataProperties;
    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private MigrationRepository migrationRepository;
    
    @Override
    @DataSource
    public Future<Integer> migration(int index, int size) {
        
        ListenableFuture<Integer> future;
        // 读数据
        List<Map<String, Object>> sourceData = migrationRepository.readData(index, size);
        if (sourceData.size() == 0) {
            CountUtil.end();
            return null;
        }
        // 格式化数据
        List<Map<String, Object>> finalData = dataFormat(sourceData);
        // 写数据，返回成功计数
        future = AsyncResult.forValue(migrationRepository.writeData(finalData, index));
        future.addCallback(new CountUtil.AddCountCallback());
        return future;
    }
    
    @Override
    @DataSource({"firstDataSource", "secondDataSource"})
    public Future<Integer> multiMigration(int index, int size) {
        
        ListenableFuture<Integer> future;
        // 读数据
        List<Map<String, Object>> sourceData = migrationRepository.readData(index, size);
        if (sourceData.size() == 0) {
            CountUtil.end();
            return null;
        }
        // 相关数据
        sourceData = migrationRepository.readDataX(index, size, sourceData);
        // 格式化数据
        List<Map<String, Object>> finalData = dataFormat(sourceData);
        // 写数据，返回成功计数
        future =  AsyncResult.forValue(migrationRepository.writeData(finalData, index));
        future.addCallback(new CountUtil.AddCountCallback());
        return future;
    }
    
    private List<Map<String, Object>> dataFormat(List<Map<String, Object>> sourceData) {
        List<Map<String, Object>> finalData = new ArrayList<>();
        List<Relationship> relationships = dataProperties.getRelationships();
        
        Map<String, Object> columnMap;
        for (Map<String, Object> sourceDatum : sourceData) {
            columnMap = new HashMap<>();
            Relationship relationship;
            for (int i = 0; i < relationships.size(); i++) {
                relationship = relationships.get(i);
                Object columnValue = relationship.getStyle().format(sourceDatum.get(relationship.getFrom()));
                columnMap.put(relationship.getTo(), columnValue);
            }
            finalData.add(columnMap);
            log.info("生成记录：{}",columnMap);
        }
        log.info("批量格式化成功！源：{}，终：{}", sourceData.size(), finalData.size());
        return finalData;
    }
    
}
