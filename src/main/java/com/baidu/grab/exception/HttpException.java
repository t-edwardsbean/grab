package com.baidu.grab.exception;

/**
 * Created by edwardsbean on 14-11-6.
 */
public class HttpException extends RuntimeException {
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
