package route;

import model.*;
import service.ServiceAuth;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Route {

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }


    public static String html(String path) {
        byte[] content = new byte[0];
        try {
            content = Files.readAllBytes(Path.of("templates", path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String r = new String(content, StandardCharsets.UTF_8);
        return r;
    }

    public static User currentUser(Request request) {
        String username;
        if (request.cookie.containsKey("session_id")) {
            String session_id = request.cookie.get("session_id");
            username = ServiceAuth.usernameFromSessionId(session_id);
        } else {
            username = "游客";
        }
        User user = ServiceAuth.userFromUsername(username);
        return user;
    }

    public static String routeMessage(Request request) {
        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String body = html("html_basic.html");
        log("routeMessage query %s", request.query);
        HashMap<String, String> data;
        if (request.method.equals("GET")) {
            data = request.query;
        } else if (request.method.equals("POST")) {
            data = request.form;
        } else {
            throw new RuntimeException(String.format("不支持的 HTTP 方法 %s", request.method));
        }
        if (data.size() > 0) {
            Message m = new Message(data.get("author"), data.get("content"));
            MessageStorage.add(m);
        }

        // body = body.replace("{messages}", messages.toString());
        body = body.replace("{messages}", MessageStorage.load().toString());
        String resposne = header + body;
        return resposne;
    }


    public static String routeIndex(Request request) {
        String message;
        User user = currentUser(request);
        String username = user.username;
        message = String.format("当前登录用户 %s", username);
        HashMap<String, String> data = new HashMap<>();
        data.put("message", message);
        String body = ReadTemplate.render(data, "index.ftlh");
        return Route.responseHTML(body);
    }

    public static String routeAdmin(Request request) {
        String message;
        User user = currentUser(request);
        String username = user.username;
        // if (user.role.equals("admin")) {
        if (user.role == UserRole.admin) {
            message = String.format("当前登录用户 %s", username);
        } else {
            message = "没有权限访问";
        }
        String body = html("index.ftlh");
        body = body.replace("{message}", message);
        String response = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response;
    }

    public static byte[] routeStatic(HashMap<String, String> query) {
        // body
        byte[] body = new byte[0];
        // localhost:9000/static?path=xxx.gif
        log("query %s", query);
        String filepath = query.get("file");
        String header;
        if (filepath.endsWith(".css")) {
            header = "HTTP/1.1 200 very OK\r\nContent-Type: text/css; charset=utf-8;\r\n\r\n";
        } else if (filepath.endsWith(".js")) {
            header = "HTTP/1.1 200 very OK\r\nContent-Type: text/javascript; charset=utf-8;\r\n\r\n";
        } else if (filepath.endsWith(".gif") || filepath.endsWith(".jpg")) {
            header = "HTTP/1.1 200 very OK\r\nContent-Type: image/gif;\r\n\r\n";
        } else {
            throw new RuntimeException(String.format("不支持的文件后缀 %s", filepath));
        }
        try {
//            body = Files.readAllBytes(Path.of("static", filepath));
            URL resource = TxtStorage.class.getResource("/static");
            String directoryPath = Path.of(resource.toURI()).toString();
            log("TxtStorage resource path<%s>", directoryPath);
            body = Files.readAllBytes(Path.of(directoryPath, filepath));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        byte[] headerBytes = header.getBytes();
        byte[] response = new byte[headerBytes.length + body.length];
        for (int i = 0; i < response.length; ++i) {
            if (i < headerBytes.length) {
                response[i] = headerBytes[i];
            } else {
                response[i] = body[i - headerBytes.length];
            }
        }
        return response;
    }

    public static String route404() {
        String body = "<html><body><h1>404</h1></body></html>";
        String response = "HTTP/1.1 404 NOT OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response;
    }

    public static String responseHTML(String body) {
        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String resposne = header + body;
        return resposne;
    }

    public static String responseHTML(String body, String cookie) {
        String header = "HTTP/1.1 200 very OK\r\n" +
                "Content-Type: text/html\r\n" +
                "%s" +
                "\r\n";
        // 如果有 cookie 就加上
        if (cookie.length() > 0) {
            header = String.format(header, cookie);
        } else {
            header = String.format(header, "");
        }
        String resposne = header + body;
        return resposne;
    }

    public static String responseRedirect(String location) {
        String response = String.format("HTTP/1.1 302 redirect\r\n" +
                "Content-Type: text/html\r\n" +
                "Location:%s\r\n\r\n", location
        );
        return response;
    }


    public static byte[] responseForRoute(Request request) {
        byte[] response;
        //noinspection IfCanBeSwitch
        if (request.route.equals("/")) {
            response = routeIndex(request).getBytes();
        } else if (request.route.equals("/admin")) {
            response = routeAdmin(request).getBytes();
        } else if (request.route.equals("/login/view")) {
            response = RouteAuth.loginView(request).getBytes();
        } else if (request.route.equals("/login")) {
            response = RouteAuth.login(request).getBytes();
        } else if (request.route.equals("/register/view")) {
            response = RouteAuth.registerView(request).getBytes();
        } else if (request.route.equals("/register")) {
            response = RouteAuth.register(request).getBytes();
        } else if (request.route.equals("/message")) {
            response = routeMessage(request).getBytes();
        } else if (request.route.equals("/todo/all")) {
            response = RouteTodo.all(request).getBytes();
        } else if (request.route.equals("/todo/add")) {
            response = RouteTodo.add(request).getBytes();
        } else if (request.route.equals("/todo/delete")) {
            response = RouteTodo.delete(request).getBytes();
        } else if (request.route.equals("/todo/edit")) {
            response = RouteTodo.edit(request).getBytes();
        } else if (request.route.equals("/todo/update")) {
            response = RouteTodo.update(request).getBytes();
        } else if (request.route.equals("/weibo/all")) {
            response = RouteWeibo.all(request).getBytes();
        } else if (request.route.equals("/weibo/timeline")) {
            response = RouteWeibo.timeline(request).getBytes();
        } else if (request.route.equals("/weibo/add")) {
            response = RouteWeibo.add(request).getBytes();
        } else if (request.route.equals("/comment/new")) {
            response = RouteWeibo.commentNew(request).getBytes();
        } else if (request.route.equals("/comment/add")) {
            response = RouteWeibo.commentAdd(request).getBytes();
        } else if (request.route.equals("/static")) {
            response = routeStatic(request.query);
        } else {
            response = route404().getBytes();
        }
        return response;
    }
}
