package com.mugwort.spring.advice;

public class MismatchException extends RuntimeException {

    public MismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
