package com.oyster.exercise.exception;

public class InsufficientFundsException extends RuntimeException {
    private final String message;

    public InsufficientFundsException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

