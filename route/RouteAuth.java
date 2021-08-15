package route;

import model.*;
import service.ServiceAuth;

import java.util.HashMap;

public class RouteAuth {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static String loginView(Request request) {
        User user = Route.currentUser(request);
        String username = user.username;
        String message = String.format("当前登录用户 %s", username);
        HashMap<String, String> data = new HashMap<>();
        data.put("message", message);
        String body = ReadTemplate.render(data, "login.ftlh");
        return Route.responseHTML(body);
    }


    public static String login(Request request) {
        String message;
        String cookie;
        String username = request.query.get("username");
        String password = request.query.get("password");
        String sessionId = ServiceAuth.login(username, password);
        if (sessionId.length() > 0) {
            cookie = String.format("Set-Cookie: session_id=%s\r\n", sessionId);
            message = String.format("登录成功 %s", username);
        } else {
            cookie = "";
            message = "登录失败";
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("message", message);
        String body = ReadTemplate.render(data, "login.ftlh");
        return Route.responseHTML(body, cookie);
    }

    public static String registerView(Request request) {
        HashMap<String, String> data = new HashMap<>();
        data.put("message", "请注册");
        String body = ReadTemplate.render(data, "register.ftlh");
        return Route.responseHTML(body);
    }

    public static String register(Request request) {
        String message;
        String username = request.query.get("username");
        String password = request.query.get("password");
        boolean success = ServiceAuth.register(username, password);
        if (success) {
            message = String.format("注册成功 %s", username);
        } else {
            message = String.format("注册失败");
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("message", message);
        String body = ReadTemplate.render(data, "register.ftlh");
        return Route.responseHTML(body);
    }


}
