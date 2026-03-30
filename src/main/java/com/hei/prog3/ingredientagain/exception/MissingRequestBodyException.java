package com.hei.prog3.ingredientagain.exception;

public class MissingRequestBodyException extends RuntimeException {

    public MissingRequestBodyException() {
        super("Request body is required");
    }
}