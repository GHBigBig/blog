package com.zjg.blog.interceptor;

import com.zjg.blog.exception.LoginException;
import javafx.fxml.LoadException;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zjg
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 我想使用 request 方法转发到 /admin/login 但是，post 方法的请求会直接以 post 方式
     *      直接转发
     * 而 post 方法的 /admin/login 是处理登录的方法，所以不应该 reqestDispatcher.forword()
     * reponse.sendRedirect 方法我无法传递提示信息
     *
     * 所以我这里才有抛异常方式
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws LoginException {
        if (request.getSession().getAttribute("user") == null) {
//            request.setAttribute("message", "请登录");
//            request.getRequestDispatcher("/admin/login").forward(request, response);


//            response.sendRedirect("/admin/login?message=login---");

            throw new LoginException();
        }
        return true;
    }
}
