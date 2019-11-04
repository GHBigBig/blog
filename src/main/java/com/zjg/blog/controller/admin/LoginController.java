package com.zjg.blog.controller.admin;

import com.zjg.blog.pojo.User;
import com.zjg.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author zjg
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * Get 方式的 /admin/login 请求，只是显示页面
     * @return 管理员登录页面
     */
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    /**
     * Post 方式的 /admin/login 请求，处理用户登录
     * 成功进入欢迎页
     * 失败重定向到管理员登录页面
     * @param username  用户名
     * @param password  用户密码
     * @param session  会话对象
     * @param attributes  重定向时携带数据
     * @return
     */
    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, RedirectAttributes attributes) {
        User user = loginService.login(username, password);
        if (user != null) {
            user.setUserPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/admin/login";
        }
    }

    /**
     * 用户退出，将 session 中的 user 对象移除
     * 并重定向到管理员登录页面
     * @param session
     * @return  重定向到管理员登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin/login";
    }
}
