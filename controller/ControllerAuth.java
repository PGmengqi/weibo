package web23.web18.controller;

import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import route.ReadTemplate;
import route.Request;
import route.Route;
import service.ServiceAuth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class ControllerAuth {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    @GetMapping("/login/view")
    public static ModelAndView loginView(HttpServletRequest request) {
        User user = ControllerHelper.currentUser(request);
        String username = user.username;
        String message = String.format("当前登录用户 %s", username);
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @PostMapping("/login")
    public static String login(String username, String password, HttpServletResponse response) {
        String message;
        String sessionId = ServiceAuth.login(username, password);
        Cookie cookie;
        if (sessionId.length() > 0) {
            // cookie = String.format("Set-Cookie: session_id=%s\r\n", sessionId);
            cookie = new Cookie("session_id", sessionId);
            message = String.format("登录成功 %s", username);
        } else {
            cookie = new Cookie("", "");
            message = "登录失败";
        }
        response.addCookie(cookie);
//        ModelAndView modelAndView = new ModelAndView("login");
//        modelAndView.addObject("message", message);
//        return modelAndView;
        return "redirect:/login/view";
    }

    public static String registerView(Request request) {
        HashMap<String, String> data = new HashMap<>();
        data.put("message", "请注册");
        String body = ReadTemplate.render(data, "register.ftlh");
        return Route.responseHTML(body);
    }

    public static String register(Request request) {
        String message;
        String username = request.form.get("username");
        String password = request.form.get("password");
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
