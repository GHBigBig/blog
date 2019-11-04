package com.zjg.blog.exception;

/**
 * 主要处理用户认证异常的，包括未登录，权限不足
 * 可以生成子类接口更清晰
 * @author zjg
 */
public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = -6584557542403596593L;

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    protected AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
