package com.example.mysqlmarkdown.doc;

import java.util.List;

public class Table {
    // 数据库表名
    private String tableName;
    // 服务端model名
    private String objectName;
    // 数据库表的建表语句
    private String comment;
    // 表包含的字段
    private List<Column> columns;

    public Table(String tableName, String objectName) {
        this.tableName = tableName;
        this.objectName = objectName;
    }
    // 此处省略getter setter

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
