package com.blithe.crm.exception;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/9 14:05
 * Description:
 */

public class ChangeException extends RuntimeException{
    public ChangeException(String message) {
        super(message);
    }

    public ChangeException() {
        super();
    }
}
