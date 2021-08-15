package route;

import model.User;
import model.Weibo;
import service.ServiceTodo;
import service.ServiceWeibo;

import java.util.ArrayList;
import java.util.HashMap;

public class RouteWeibo {
    public static String timeline(Request request) {
        User currentUser = Route.currentUser(request);

        ArrayList<Weibo> weibos = ServiceWeibo.timelineWeibos(currentUser);
        // 读出 模板 文件并把数据放进 html 文件
        HashMap<String, Object> data = new HashMap<>();
        data.put("weibos", weibos);
        String body = ReadTemplate.render(data, "weibo_all.ftlh");
        return Route.responseHTML(body);
    }


    public static String all(Request request) {
        // 显示所有 todo
        User currentUser = Route.currentUser(request);

        ArrayList<Weibo> weibos = ServiceWeibo.currentUserWeibos(currentUser);
        // 读出 模板 文件并把数据放进 html 文件
        HashMap<String, Object> data = new HashMap<>();
        data.put("weibos", weibos);
        String body = ReadTemplate.render(data, "weibo_all.ftlh");
        return Route.responseHTML(body);
    }

    public static String add(Request request) {
        // 添加 todo 路由
        // 拿到请求带上的数据
        // 取数据
        Route.log("routeWeibo query %s", request.query);
        if (request.query.size() > 0) {
            String content = request.query.get("content");
            User currentUser = Route.currentUser(request);
            // 处理数据
            ServiceWeibo.add(content, currentUser);
        }
        // 返回数据
        return Route.responseRedirect("/weibo/all");
    }

    public static String commentNew(Request request) {
        int id = Integer.parseInt(request.query.get("id"));
        // 读出 模板 文件并把数据放进 html 文件
        HashMap<String, Object> data = new HashMap<>();
        data.put("weibo_id", id);
        String body = ReadTemplate.render(data, "comment_new.ftlh");
        return Route.responseHTML(body);

    }

    public static String commentAdd(Request request) {
        Route.log("routeWeibo query %s", request.query);
        if (request.query.size() > 0) {
            String content = request.query.get("content");
            int weiboId = Integer.parseInt(request.query.get("id"));
            User currentUser = Route.currentUser(request);
            // 处理数据
            ServiceWeibo.commentAdd(content, currentUser, weiboId);
        }
        // 返回数据
        return Route.responseRedirect("/weibo/all");
    }

    public static String delete(Request request) {
        // 拿数据
        int id = Integer.parseInt(request.query.get("id"));
        User user = Route.currentUser(request);
        // 处理数据
        if (ServiceTodo.currentUserTodo(id, user.id)) {
            ServiceTodo.delete(id);
        }
        // 返回数据
        return Route.responseRedirect("/todo/all");
    }

    public static String edit(Request request) {
        // 拿到请求 id
        int id = Integer.parseInt(request.query.get("id"));
        User user = Route.currentUser(request);
        if (ServiceTodo.currentUserTodo(id, user.id)) {
            // 读出 模板 文件并把数据放进 html 文件
            HashMap<String, Object> data = new HashMap<>();
            data.put("todo_id", id);
            String body = ReadTemplate.render(data, "todo_edit.ftlh");
            return Route.responseHTML(body);
        } else {
            return Route.responseRedirect("/todo/all");
        }

    }

    public static String update(Request request) {
        // 拿到请求数据
        int id = Integer.parseInt(request.query.get("id"));
        String content = request.query.get("content");
        User user = Route.currentUser(request);
        if (ServiceTodo.currentUserTodo(id, user.id)) {
            // 处理数据
            ServiceTodo.update(id, content);
        }
        // 返回数据
        return Route.responseRedirect("/todo/all");
    }
}
