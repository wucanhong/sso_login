package com.sso.controller;

import com.sso.mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserController
 * @Author 吴灿洪
 * @Description
 * @Date 2019/12/7 9:43
 * @Version 1.0
 */
@RestController
public class UserLoginController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/notLogin")
    public String notLogin() {
        return "您没有登录！";
    }

    @GetMapping("/notRole")
    public String notRole() {
        return "您没有权限！";
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        return "成功注销";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpServletRequest request, HttpServletResponse response) {


//        HttpSession session = request.getSession();
//        session.setAttribute("username", username);
//        session.setMaxInactiveInterval(1000);
//        String a = (String) session.getAttribute("username");
//        System.out.println("**************************" + a);
//        Cookie[] cookies = request.getCookies();


//        Map<String, String> map = new HashMap<>();
//        Arrays.asList(cookies).forEach(cookie -> map.put(cookie.getName(), cookie.getValue()));
//
//        if (map.containsKey("username") && map.containsKey("password")) {
//            username = map.get("username");
//            password = map.get("password");
//        }

        //从SecurityUtils里面创建一个subject
        Subject subject = SecurityUtils.getSubject();
        //在认证提交前准备token(令牌)
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //执行认证登录
        token.setRememberMe(true);
        subject.login(token);


//        Cookie cookies1 = new Cookie("username", username);
//        cookies1.setMaxAge(1000);
//        Cookie cookies2 = new Cookie("password", password);
//        cookies2.setMaxAge(1000);
//        response.addCookie(cookies1);
//        response.addCookie(cookies2);

        //根据权限，指定返回数据
        String role = userMapper.getRole(username);
        if ("user".equals(role)) {
            return "欢迎用户登录";
        }
        if ("admin".equals(role)) {
            return "欢迎管理员登录";
        }
        return "权限错误";
    }
}