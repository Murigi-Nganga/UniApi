package com.example.uniapi.exception;

// Custom exception class for invalid request parameter values in GET requests
public class InvalidReqParamException extends RuntimeException{

    public InvalidReqParamException(String message) {
        super(message);
    }
}
