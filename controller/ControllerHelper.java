package web23.web18.controller;

import model.User;
import route.Request;
import service.ServiceAuth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ControllerHelper {
    public static User currentUser(HttpServletRequest request) {
        String username;
        Cookie[] cookies = request.getCookies();
        boolean found = false;
        String session_id = "";
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals("session_id")) {
                session_id = cookie.getValue();
                found = true;
            }
        }
        if (found) {
            username = ServiceAuth.usernameFromSessionId(session_id);
        } else {
            username = "游客";
        }

        User user = ServiceAuth.userFromUsername(username);
        return user;
    }
}
