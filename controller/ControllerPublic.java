package web23.web18.controller;

import model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import route.*;
import service.ServiceAuth;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

@Controller
public class ControllerPublic {

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    @GetMapping("/index")
    public static ModelAndView index(HttpServletRequest request) {
        String message;
        User user = ControllerHelper.currentUser(request);
        String username = user.username;
        message = String.format("当前登录用户 %s", username);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

}
