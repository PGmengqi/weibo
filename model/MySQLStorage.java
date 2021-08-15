package model;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;

public class MySQLStorage {

    private static MysqlDataSource source;

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    static {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("zaoshuizaoqi");
        dataSource.setServerName("127.0.0.1");
        dataSource.setDatabaseName("web");


        try {
            // 本地开发不用加密链接
            dataSource.setUseSSL(false);
            // 数据库连接的编码
            dataSource.setCharacterEncoding("UTF-8");
            // 没有枚举 只能翻文档猜出时区
            dataSource.setServerTimezone("Asia/Shanghai");

            log("url: %s", dataSource.getUrl());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        source = dataSource;
    }

    public static <T> void update(String sql, MySQLSerializer<T> serializer) {
        // 可以执行 INSERT UPDATE DELETE 语句
        MysqlDataSource ds = source;
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            serializer.serialize(statement);
            log("update %s", statement.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> ArrayList<T> select(String sql, MySQLDeserializer<T> deserializer) {
        // 可以执行 SELECT 语句
        MysqlDataSource ds = source;
        ArrayList<T> results = new ArrayList<>();
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            log("select %s", statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T result = deserializer.deserialize(resultSet);
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return results;
    }

    public static <T> ArrayList<T> select(String sql, MySQLDeserializer<T> deserializer, MySQLSerializer<T> serializer) {
        MysqlDataSource ds = source;
        ArrayList<T> results = new ArrayList<>();
        try (
                Connection connection = ds.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            serializer.serialize(statement);
            log("select %s", statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T result = deserializer.deserialize(resultSet);
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return results;
    }

}
