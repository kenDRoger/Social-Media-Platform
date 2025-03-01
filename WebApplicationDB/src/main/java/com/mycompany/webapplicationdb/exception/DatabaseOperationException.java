package com.mycompany.webapplicationdb.exception;

public class DatabaseOperationException extends Exception {

    public DatabaseOperationException(String message, Throwable cause) {
        super(message,cause);
    }
    //add String message as parameters in const
}
