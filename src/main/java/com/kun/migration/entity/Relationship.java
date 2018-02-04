package com.kun.migration.entity;


import com.kun.migration.constants.Style;

/**
 * @author CaoZiye
 * @version 1.0 2017/12/4 9:48
 */
public class Relationship {
    
    private String from;
    private String to;
    private Style style;
    
    public Relationship() {
    }
    
    public Relationship(String from, String to, Style style) {
        this.from = from;
        this.to = to;
        this.style = style;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public Style getStyle() {
        return style;
    }
    
    public void setStyle(Style style) {
        this.style = style;
    }
    
    @Override
    public String toString() {
        return "Relationship{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", style=" + style +
                '}';
    }
}
