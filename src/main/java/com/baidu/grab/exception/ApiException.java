package com.baidu.grab.exception;

/**
 * Created by edwardsbean on 14-11-6.
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {

        super(message, cause);
    }
}
