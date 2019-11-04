package com.zjg.blog.handler;

import com.zjg.blog.exception.LoginException;
import com.zjg.blog.exception.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zjg
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 若 LoginInterceptor 检测到用户未登录抛出异常，
     * 此方法捕捉到，请 redirect 到 /admin/login 并带异常信息
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(LoginException.class)
    public String loginExceptionHandler(HttpServletResponse response, RedirectAttributes attributes, LoginException e){

        attributes.addFlashAttribute("message", "请登录");

        return "redirect:/admin/login";
    }

    /**
     * 默认异常处理: 发送错误通过 error 页面返回信息
     * @param request
     * @param e
     * @return modelAndView
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("method", request.getMethod());
        modelAndView.addObject("exception", e);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }

    /**
     * 自定义的 NotFoundException 异常处理
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundExceptionHandler(HttpServletRequest request, ModelAndView modelAndView,
                                                 NotFoundException e) {
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("exception", e);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}
