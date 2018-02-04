package com.kun.migration.dao;

import com.kun.migration.aspect.DataSourceSwitch;
import com.kun.migration.configuration.properties.DataProperties;
import com.kun.migration.entity.Relationship;
import com.kun.migration.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 15:28
 */
@Repository
public class MigrationRepositoryImpl implements MigrationRepository{
    
    private static final Logger log = LoggerFactory.getLogger(MigrationRepositoryImpl.class);
    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private DataProperties dataProperties;
    
    @DataSourceSwitch
    @Override
    public List<Map<String, Object>> readData(int index, int size) {
        Connection connection = jdbcUtil.getConnection();
        List<Map<String, Object>> list = new LinkedList<>();
        try {
            String sql = dataProperties.getQuerySql();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, index);
            preparedStatement.setInt(2, size);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            Map<String, Object> map;
            while (resultSet.next()) {
                map = new HashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
                }
                list.add(map);
            }
            log.info("读取记录成功，index：{}，size：{}", index, list.size());
            return list;
        } catch (SQLException e) {
            log.info("读取记录失败，index：{}，size：{}", index, size);
            throw new RuntimeException("出现SQL异常", e);
        }
    }
    
    @DataSourceSwitch
    @Override
    public Integer writeData(List<Map<String, Object>> dataList, int index) {
        Connection connection = jdbcUtil.getConnection();
        try {
            String sql = jdbcUtil.getWriteSql(dataList);
    
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            List<Relationship> relationships = dataProperties.getRelationships();
            int count = 0;
            for (Map<String, Object> datum : dataList) {
                for (int i = 0; i < relationships.size(); i++) {
                    Object value = datum.get(relationships.get(i).getTo());
                    preparedStatement.setObject(++count, value);
                }
            }
    
            preparedStatement.execute();
            log.info("写入记录成功！index：{}，size：{}", index, dataList.size());
            return dataList.size();
            
        } catch (SQLException e) {
            log.info("写入记录失败！index：{}，size：{}", index, dataList.size());
            throw new RuntimeException("出现SQL异常", e);
        }
    }
    
    @Override
    @DataSourceSwitch("secondDataSource")
    public List<Map<String, Object>> readDataX(int index, int size, List<Map<String, Object>> appendList) {
        Connection connection = jdbcUtil.getConnection();
        List<Map<String, Object>> finalList = new ArrayList<>(appendList.size());
        try {
            String relKey = dataProperties.getRelKey();
            List<Object> valueList = new ArrayList<>();
            
            appendList.stream().map((map) ->
                map.get(relKey)
            ).forEach((value) -> {
                if (value != null) {
                    valueList.add(value);
                }
            });
            
            PreparedStatement preparedStatement = connection.prepareStatement(jdbcUtil.getReadRefSql(valueList));
    
            for (int i = 0; i < valueList.size(); i++) {
                preparedStatement.setObject(i + 1, valueList.get(i));
            }
            
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
    
            while (resultSet.next()) {
                for (int i1 = 0; i1 < appendList.size(); i1++) {
                    Map<String, Object> map = appendList.get(i1);
                    if (resultSet.getObject(relKey).equals(map.get(relKey))) {
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
                        }
                        finalList.add(appendList.remove(i1));
                        break;
                    }
                }
            }
            // 否则，对于主表中relKey值缺失的记录会遗失
            finalList.addAll(appendList);
            //----------------
            log.info("读取相关记录成功，index：{}，size：{}", index, valueList.size());
            return finalList;
        } catch (SQLException e) {
            log.info("读取记录失败，index：{}，size：{}", index, size);
            throw new RuntimeException("出现SQL异常", e);
        }
    }
    
}
