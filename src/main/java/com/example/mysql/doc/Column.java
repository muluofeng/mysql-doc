package com.example.mysql.doc;

public class Column {
    // 数据库字段名称
    private String field;
    // 服务端model属性名称
    private String param;
    // 数据库字段类型
    private String type;
    // 数据库字段注释
    private String comment;

    //默认值
    private String defaultVal;

    //主键 外键  唯一约束
    private String key;

    //自增 ...
    private String extra;

    //是否允许为空
    private String allowNull;

    public Column(String field, String param, String type, String comment) {
        this.field = field;
        this.param = param;
        this.type = type;
        this.comment = comment;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(String allowNull) {
        this.allowNull = allowNull;
    }
}
