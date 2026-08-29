package com.roh.jwtApplication.CustomExceptionHandler;

public class JwtAuthenticationException extends RuntimeException{

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
