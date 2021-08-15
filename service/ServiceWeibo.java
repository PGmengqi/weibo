package service;

import model.*;

import java.util.ArrayList;

public class ServiceWeibo {
    public static ArrayList<Weibo> currentUserWeibos(User currentUser) {
        String sql = "SELECT * FROM weibo WHERE userId = ?";
        ArrayList<Weibo> weibos = MySQLStorage.select(
                sql,
                resultSet -> {
                    int id = resultSet.getInt("id");
                    String content = resultSet.getString("content");
                    int userId = resultSet.getInt("userId");
                    return new Weibo(id, content, userId);
                },
                statement -> {
                    statement.setInt(1, currentUser.id);
                }
        );
        for (int i = 0; i < weibos.size(); i++) {
            Weibo weibo = weibos.get(i);
            String commentSelect = "SELECT * FROM comment WHERE weiboId = ?";
            ArrayList<Comment> comments = MySQLStorage.select(
                    commentSelect,
                    resultSet -> {
                        int id = resultSet.getInt("id");
                        String content = resultSet.getString("content");
                        int userId = resultSet.getInt("userId");
                        return new Comment(id, content, userId, weibo.getId());
                    },
                    statement -> {
                        statement.setInt(1, weibo.getId());
                    }
            );
            weibo.setComments(comments);
        }

        return weibos;
    }

    public static ArrayList<Weibo> timelineWeibos(User currentUser) {
        String sql = "select * from weibo " +
                "join follow on follow.followeeId = weibo.userId " +
                "where follow.followerId = ?";
        ArrayList<Weibo> weibos = MySQLStorage.select(
                sql,
                resultSet -> {
                    int id = resultSet.getInt("id");
                    String content = resultSet.getString("content");
                    int userId = resultSet.getInt("userId");
                    return new Weibo(id, content, userId);
                },
                statement -> {
                    statement.setInt(1, currentUser.id);
                }
        );
        for (int i = 0; i < weibos.size(); i++) {
            Weibo weibo = weibos.get(i);
            String commentSelect = "SELECT * FROM comment WHERE weiboId = ?";
            ArrayList<Comment> comments = MySQLStorage.select(
                    commentSelect,
                    resultSet -> {
                        int id = resultSet.getInt("id");
                        String content = resultSet.getString("content");
                        int userId = resultSet.getInt("userId");
                        return new Comment(id, content, userId, weibo.getId());
                    },
                    statement -> {
                        statement.setInt(1, weibo.getId());
                    }
            );
            weibo.setComments(comments);
        }

        return weibos;
    }

    public static void add(String content, User currentUser) {
        String sql = "INSERT INTO weibo (content, userId) VALUES (?, ?)";
        MySQLStorage.update(sql, statement -> {
            statement.setString(1, content);
            statement.setInt(2, currentUser.id);
        });
    }

    public static void commentAdd(String content, User currentUser, int weiboId) {
        String sql = "INSERT INTO comment (content, userId, weiboId) VALUES (?, ?, ?)";
        MySQLStorage.update(sql, statement -> {
            statement.setString(1, content);
            statement.setInt(2, currentUser.id);
            statement.setInt(3, weiboId);
        });
    }

    public static void delete(int id) {
        String sql = "DELETE FROM todo WHERE id = ?";
        MySQLStorage.update(sql, statement -> {
            statement.setInt(1, id);
        });
    }

    public static void update(int id, String content) {
        String sql = "UPDATE todo SET content = ? WHERE id = ?";
        MySQLStorage.update(sql, statement -> {
            statement.setString(1, content);
            statement.setInt(2, id);
        });
    }
}
