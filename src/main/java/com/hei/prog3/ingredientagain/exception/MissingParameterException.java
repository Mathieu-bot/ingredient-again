package com.hei.prog3.ingredientagain.exception;

public class MissingParameterException extends RuntimeException {

    public MissingParameterException() {
        super("Either mandatory query parameter `at` or `unit` is not provided.");
    }
}