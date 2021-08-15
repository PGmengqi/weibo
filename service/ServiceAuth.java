package service;

import model.*;
import route.Route;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

public class ServiceAuth {

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }


    public static String hexFromBytes(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, bytesLength = bytes.length; i < bytesLength; i++) {
            byte currentByte = bytes[i];
            // 02 代表不足两位补足两位 x代表用16进制表示
            // String.format("%02x", 0) = "00"
            result.append(String.format("%02x", currentByte));
        }
        return result.toString();
    }

    public static String SaltedPasswordHash(String password, String salt) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String salted = salt + password;
        md.update(salted.getBytes(StandardCharsets.UTF_8));
        byte[] result = md.digest();
        return  hexFromBytes(result);
    }

    public static String login(String username, String password) {
        ArrayList<User> users = TxtStorage.load("User.txt", new UserDeserializer(),5);
        for (int i = 0; i < users.size(); i++) {
            User currentUser = users.get(i);
            if (currentUser.username.equals(username)) {
                String salt = currentUser.salt;
                String saltedPassword = ServiceAuth.SaltedPasswordHash(password, salt);
                if (currentUser.password.equals(saltedPassword)) {
                    String sessionId = UUID.randomUUID().toString();
                    Session session = new Session(sessionId, username);
                    TxtStorage.add(session, "Session.txt",
                            new SessionSerializer(), new SessionDeserializer(),2
                    );
                    return sessionId;
                }
            }
        }
        return "";
    }

    public static User guest() {
        User user = new User(0, "游客", "dadasds", UserRole.guest, "dasdas");
        return user;
    }

    public static String usernameFromSessionId(String sessionId) {
        ArrayList<Session> sessions = TxtStorage.load(
                "Session.txt", new SessionDeserializer(), 2
        );
        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);
            if (session.id.equals(sessionId)) {
                return session.username;
            }
        }
        return guest().username;
    }

    public static User userFromUsername(String username) {
        ArrayList<User> users = TxtStorage.load("User.txt", new UserDeserializer(), 5);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.username.equals(username)) {
                return user;
            }
        }
        return guest();
    }

    public static boolean register(String username, String password) {
        ArrayList<User> users = TxtStorage.load("User.txt", new UserDeserializer(), 5);
        int id = users.size() + 1;
        String salt = UUID.randomUUID().toString();
        String saltedPassword = ServiceAuth.SaltedPasswordHash(password, salt);
        User user = new User(id, username, saltedPassword, UserRole.normal, salt);
        try {
            // model.UserStorage.add(user);
            TxtStorage.add(user, "User.txt", new UserSerializer(), new UserDeserializer(), 5);
            return true;
        } catch (RuntimeException e) {
            log(e.toString());
            return false;
        }

    }
}
