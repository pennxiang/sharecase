package com.sharedcase.exception;

/**
 * ClassName: ChainWriteException
 * Package: com.sharedcase.exception
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/20 10:16
 */
public class ChainWriteException extends Exception {
    public ChainWriteException(String message) {
        super(message);
    }

    public ChainWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
