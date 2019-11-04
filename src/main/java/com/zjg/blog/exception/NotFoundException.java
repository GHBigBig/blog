package com.zjg.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author zjg
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = 3141950935502257067L;

    //抛出异常还是捕获异常根据：能不能确定如何处理这种异常，是否有处理需求
    /*
         一般地，用户自定义异常类都是RuntimeException的子类。
         自定义异常类通常需要编写几个重载的构造器。
         自定义异常需要提供serialVersionUID
         自定义的异常通过throw抛出。
         自定义异常最重要的是异常类的名字，当异常出现时，可以根据
        名字判断异常类型。
     */


    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
