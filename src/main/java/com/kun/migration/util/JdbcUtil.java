package com.kun.migration.util;

import com.kun.migration.configuration.properties.DataProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 10:42
 */
@Component
public class JdbcUtil {
    
    private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);
    @Autowired
    private DataProperties dataProperties;
    @Autowired
    private Map<String, DataSource> dataSourceMap;
    
    private static ThreadLocal<HashMap<String, Connection>> connectionMap = new ThreadLocal<>();
    private static ThreadLocal<Connection> currentConnection = new ThreadLocal<>();
    
    public Connection start(String... dataSourceNames) {
        Connection connection = null;
        HashMap<String, Connection> connectionMap = new HashMap<>();
        try {
            for (int i = 0; i < dataSourceNames.length; i++) {
                connection = dataSourceMap.get(dataSourceNames[i]).getConnection();
                connection.setAutoCommit(false);
                connectionMap.put(dataSourceNames[i], connection);
                JdbcUtil.connectionMap.set(connectionMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public void commit() {
        connectionMap.get().forEach((name, connection) -> {
            if (connection != null) {
                try {
                    connection.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        connectionMap.remove();
        currentConnection.remove();
    }
    
    public void rollBack() {
        connectionMap.get().forEach((name, connection) -> {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        connectionMap.remove();
        currentConnection.remove();
    }
    
    public void switchConnection(String dataSourceName){
        Connection connection = connectionMap.get().get(dataSourceName);
        if (connection == null) {
            throw new RuntimeException("该数据源未启用！");
        }
        currentConnection.set(connection);
    }
    
    public Connection getConnection() {
        return currentConnection.get();
    }
    
    @Cacheable(cacheNames = "writeSqlCache", key = "#dataList.size()")
    public String getWriteSql(List<Map<String, Object>> dataList) {
        
        StringBuilder sbSql = new StringBuilder(dataProperties.getUpsertSql());
    
        for (int i = 0, size = dataList.size(); i < size; i++) {
            sbSql.append("(");
            for (int y = 0, count = dataProperties.getTargetFields().size(); y < count; y++) {
                sbSql.append("?,");
            }
            sbSql.deleteCharAt(sbSql.length() - 1);
            sbSql.append("),");
        }
        sbSql.deleteCharAt(sbSql.length() - 1);
    
        log.info("write sql generated:{}", sbSql);
    
        return sbSql.toString();
    }
    
    @Cacheable(cacheNames = "readRefSqlCache", key = "#valueList.size()")
    public String getReadRefSql(List<Object> valueList) {
        
        StringBuilder sb = new StringBuilder(dataProperties.getRelSql());
        
        sb.append("(");
        for (int i = 0, size = valueList.size(); i < size; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1).append(")");
    
        log.info("read ref sql generated:{}", sb);
        
        return sb.toString();
    }
    
    @CacheEvict(cacheNames = {"writeSqlCache", "readRefSqlCache"}, allEntries = true)
    public void clearCache() {
    }
    
}
