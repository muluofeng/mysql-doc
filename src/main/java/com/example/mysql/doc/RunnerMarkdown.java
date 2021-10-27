package com.example.mysql.doc;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 读取mysql数据库下表的结构信息
 */
public class RunnerMarkdown {


    private static String IP;
    private static String PORT;
    private static String DBANAME;
    private static String USERNAME;
    private static String PWD;
    private static String TITLE;
    private static String JDBC_URL;

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = RunnerWord.class.getClassLoader().getResourceAsStream("config.properties");
        // 使用properties对象加载输入流
        BufferedReader bf = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        properties.load(bf);

        //获取key对应的value值
        String format = String.format("jdbc:mysql://%s:%s/%s?allowMultiQueries=true&useUnicode=true&useSSL=false&zeroDateTimeBehavior=convertToNull",
                properties.getProperty("ip"), properties.getProperty("port"), properties.getProperty("dbname"));

        IP = properties.getProperty("ip");
        PORT = properties.getProperty("port");
        DBANAME = properties.getProperty("dbname");
        JDBC_URL = format;
        USERNAME = properties.getProperty("username");
        PWD = properties.getProperty("password");
        TITLE = properties.getProperty("title");


        // 获取数据库下的所有表名称
        List<Table> tables = getAllTableName();
        // 获得表的建表语句
        buildTableComment(tables);
        // 获得表中所有字段信息
        buildColumns(tables);
        // 写文件
        write(tables,TITLE);
    }

    /**
     * 写文件
     */
    private static void write(List<Table> tables,String title) {

        String path = System.getProperty("user.dir") + "/"+title+".md";

        StringBuffer fileContent = new StringBuffer();
        fileContent.append("## ").append(TITLE).append("\n\n");
        fileContent.append("[TOC]\n\n");

        for (Table table : tables) {
            System.out.println(table.getTableName());
            StringBuilder buffer = new StringBuilder();
            buffer.append("### " + table.getTableName() + " " + table.getComment() + "\n");
            buffer.append("------------\n");
            buffer.append("|参数|类型|注释|允空|键|备注|\n");
            buffer.append("|:-------|:-------|:-------|:-------|:-------|:-------|\n");
            List<Column> columns = table.getColumns();
            for (Column column : columns) {
                String param = column.getParam();
                if ("del".equals(param) || "delDtm".equals(param)) continue;
                String type = column.getType();
                String comment = column.getComment();
                String key = column.getKey();

                String extra = column.getExtra();

                StringBuffer remark = new StringBuffer();

                if ("auto_increment".equals(extra)) {
                    remark.append("自增;");
                }

                comment = ("".equals(comment) ? "无" : comment.replaceAll("\r\n", "").replaceAll("\n", ""));
                buffer.append("|")
                        .append(param)
                        .append("|")
                        .append(type)
                        .append("|")
                        .append(comment)
                        .append("|")
                        .append("NO".equals(column.getAllowNull()) ? "否" : "是")
                        .append("|")
                        .append("PRI".equals(key) ? "主键" : "")
                        .append("|")
                        .append(remark.toString())
                        .append("|\n");
            }
            fileContent.append(buffer.toString().replaceAll("'", "\""));
            fileContent.append("\n\n\n");

        }
        try {
            FileUtils.writeStringToFile(new File(path), fileContent.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接数据库
     */
    private static Connection getMySQLConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(JDBC_URL,
                USERNAME, PWD);
        return conn;
    }

    /**
     * 获取当前数据库下的所有表名称
     */
    private static List<Table> getAllTableName() throws Exception {
        List<Table> tables = new ArrayList<>();
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES");
        while (rs.next()) {
            String tableName = rs.getString(1);
            String objectName = camelCase(tableName);
            Table table = new Table(tableName, objectName);
            tables.add(table);
        }
        rs.close();
        stmt.close();
        conn.close();
        return tables;
    }

    /**
     * 获得某表的建表语句
     */
    private static void buildTableComment(List<Table> tables) throws Exception {
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        for (Table table : tables) {
            ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table.getTableName());
            if (rs != null && rs.next()) {
                String createDDL = rs.getString(2);
                String comment = parse(createDDL);
                table.setComment(comment);
            }
            if (rs != null) rs.close();
        }
        stmt.close();
        conn.close();
    }

    /**
     * 获得某表中所有字段信息
     */
    private static void buildColumns(List<Table> tables) throws Exception {
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        for (Table table : tables) {
            List<Column> columns = new ArrayList<>();
            ResultSet rs = stmt.executeQuery("show full columns from " + table.getTableName());
            if (rs != null) {
                while (rs.next()) {
                    String field = rs.getString("Field");
                    String type = rs.getString("Type");
                    String comment = rs.getString("Comment");
                    String key = rs.getString("Key");
                    String defaultVal = rs.getString("Default");
                    String extra = rs.getString("Extra");
                    String allowNull = rs.getString("Null");
                    Column column = new Column(field, camelCase(field), type, comment);
                    column.setKey(key);
                    column.setDefaultVal(defaultVal);
                    column.setExtra(extra);
                    column.setAllowNull(allowNull);
                    columns.add(column);
                }
            }
            if (rs != null) {
                rs.close();
            }
            table.setColumns(columns);
        }
        stmt.close();
        conn.close();
    }

    /**
     * 返回注释信息
     */
    private static String parse(String all) {
        String comment;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }

    /**
     * 例如：employ_user_id变成employUserId
     */
    private static String camelCase(String str) {
        String[] str1 = str.split("_");
        int size = str1.length;
        String str2;
        StringBuilder str4 = null;
        String str3;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                str2 = str1[i];
                str4 = new StringBuilder(str2);
            } else {
                str3 = initcap(str1[i]);
                str4.append(str3);
            }
        }
        return str4.toString();
    }

    /**
     * 把输入字符串的首字母改成大写
     */
    private static String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

}
