package com.witalo.librayapi.services.exceptions;

public class InvalidBookGenreException extends RuntimeException {
    public InvalidBookGenreException(String message) {
        super(message);
    }
}
