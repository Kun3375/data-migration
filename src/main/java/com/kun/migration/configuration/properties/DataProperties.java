package com.kun.migration.configuration.properties;

import com.kun.migration.entity.Relationship;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/1 14:38
 */
@ConfigurationProperties(prefix = "data")
public class DataProperties implements InitializingBean {
    
    private String querySql;
    private String upsertSql;
    private String relSql;
    private List<Relationship> relationships;
    private Set<String> targetFields;
    private String aesKey;
    private String relKey;
    
    public String getQuerySql() {
        return querySql;
    }
    
    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
    
    public String getUpsertSql() {
        return upsertSql;
    }
    
    public void setUpsertSql(String upsertSql) {
        this.upsertSql = upsertSql;
    }
    
    public List<Relationship> getRelationships() {
        return relationships;
    }
    
    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }
    
    public Set<String> getTargetFields() {
        return targetFields;
    }
    
    public void setTargetFields(Set<String> targetFields) {
        this.targetFields = targetFields;
    }
    
    public String getAesKey() {
        return aesKey;
    }
    
    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }
    
    public String getRelSql() {
        return relSql;
    }
    
    public void setRelSql(String relSql) {
        this.relSql = relSql;
    }
    
    public String getRelKey() {
        return relKey;
    }
    
    public void setRelKey(String relKey) {
        this.relKey = relKey;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        
        if (querySql == null) {
            throw new RuntimeException("请输入查询语句！");
        }
        if (upsertSql == null) {
            throw new RuntimeException("请输入插入/更新语句！");
        }
        if (relationships == null || relationships.size() == 0) {
            throw new RuntimeException("请输入映射关系！");
        }
    
        targetFields = new HashSet<>();
        relationships.forEach((relationship) ->
            targetFields.add(relationship.getTo())
        );
        
//        int columnsCount = 0;
//        int fromIndex = 0;
//        while ((fromIndex = upsertSql.indexOf("?", fromIndex) + 1) != 0) {
//            columnsCount++;
//        }
//        if (columnsCount != targetFields.size()) {
//            throw new RuntimeException("映射规则条目数与插入/更新字段数不一致！");
//        }
        for (String targetField : targetFields) {
            if(!upsertSql.contains(targetField)) {
                throw new RuntimeException("映射规则中目标字段不存在！");
            }
        }
        
        relationships.sort(Comparator.comparingInt(o -> upsertSql.indexOf(o.getTo())));
        
    }
    
}
