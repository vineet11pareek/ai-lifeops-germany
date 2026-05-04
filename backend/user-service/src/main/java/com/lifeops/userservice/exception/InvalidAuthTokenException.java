package com.lifeops.userservice.exception;

public class InvalidAuthTokenException extends RuntimeException{
    public InvalidAuthTokenException(String message) {
        super(message);
    }
}
