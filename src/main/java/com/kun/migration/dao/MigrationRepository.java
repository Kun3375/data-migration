package com.kun.migration.dao;

import java.util.List;
import java.util.Map;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/9 17:34
 */
public interface MigrationRepository {
    
    List<Map<String, Object>> readData(int index, int size);
    
    Integer writeData(List<Map<String, Object>> dataList, int index);
    
    List<Map<String, Object>> readDataX(int index, int size, List<Map<String, Object>> appendList);
    
    
}
