package com.blithe.crm.exception;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/10 10:36
 * Description:
 */

public class DeleteException extends RuntimeException{
    public DeleteException() {
        super();
    }

    public DeleteException(String message) {
        super(message);
    }
}
