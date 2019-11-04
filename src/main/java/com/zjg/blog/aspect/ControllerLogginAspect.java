package com.zjg.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author zjg
 * 日志切面
 */
@Component
@Aspect
@Order(1)   //数值越小，执行的级别越高
public class ControllerLogginAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
        定义一个方法, 用于声明切入点表达式. 一般地, 该方法中再不需要添入其他的代码.
        使用 @Pointcut 来声明切入点表达式.
        后面的其他通知直接使用方法名来引用当前的切入点表达式.
        切入点表达式可以通过操作符 &&, ||, ! 结合起来.
        不包括子包，不过可以使用  ..  表示当前包及其子包
     */
//    @Pointcut("execution(* com.zjg.blog.controller.*.*(..) ) || execution(* com.zjg.blog.handler.*.*(..))")
    @Pointcut("execution(* com.zjg.blog.controller..*.*(..) )")
    private void declareJointPointExpression() {}

    @Before("declareJointPointExpression()")
    public void logBefore(JoinPoint joinPoint) {
        //获取方法名
        String methodName = joinPoint.getSignature().getName()+"()";
        //获取参数
        Object[] args = joinPoint.getArgs();
        //获取 ip 和 url
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();

        LogVO logVO = new LogVO(url, ip, methodName, args);

        logger.info(logVO.toString());
    }

    //后置通知抛不抛异常都会执行
    /*@After("declareJointPointExpression()")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("{}() ends args: ", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }*/

    /*
        返回通知，连接点返回的时候起作用
        在方法法正常结束受执行的代码
        返回通知是可以访问到方法的返回值的!
     */
    /*@AfterReturning(value = "declareJointPointExpression()", returning = "result")
    public void logAfterRunning(JoinPoint joinPoint, Object result) {
        logger.info("{}() return: {}" ,joinPoint.getSignature().getName(), result);
    }*/

    /**
     * 在目标方法出现异常时会执行的代码.
     * 可以访问到异常对象; 且可以指定在出现特定异常时在执行通知代码
     */
        /*@AfterThrowing(value = "declareJointPointExpression()", throwing = "e")
        public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
            logger.warn("{}() occurs excetion: {}", joinPoint.getSignature().getName(), e);
        }*/

    /**
     * 环绕通知需要携带 ProceedingJoinPoint 类型的参数.
     * 环绕通知类似于动态代理的全过程: ProceedingJoinPoint 类型的参数可以决定是否执行目标方法.
     * proceed() 方法来执行被代理的方法
     * 且环绕通知必须有返回值, 返回值即为目标方法的返回值
     */
    /*@Around("declareJointPointExpression()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        String methodName = proceedingJoinPoint.getSignature().getName();
        try {
            //proceed() 方法前相当于前置通知
            logger.info("{}() args: {}", methodName, proceedingJoinPoint.getArgs());
            result = proceedingJoinPoint.proceed();
            //proceed() 后面的相当于返回通知
            logger.info("{}() return: {}", methodName, result);
        } catch (Throwable throwable) {
            //catch 块里的相当于 异常通知
            logger.warn("{}() occurs exception: {}", methodName, throwable);
            throwable.printStackTrace();
        }
        //try catch 块后的相当于后置通知
        logger.debug("{}() ends", methodName);
        return result;
    }*/


    private class LogVO {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public LogVO(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return url + "::" + ip + "::classMethod=" +
                    classMethod + ":args=" + Arrays.asList(args);
        }
    }
}
