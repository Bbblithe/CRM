package com.blithe.crm.exception;

/**
 * Author:  blithe.xwj
 * Date:    2022/4/8 20:26
 * Description:
 */

public class SaveException extends RuntimeException{
    public SaveException(String message) {
        super(message);
    }

    public SaveException() {
        super();
    }
}
