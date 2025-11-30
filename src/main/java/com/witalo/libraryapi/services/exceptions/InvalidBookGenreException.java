package com.witalo.libraryapi.services.exceptions;

public class InvalidBookGenreException extends RuntimeException {
    public InvalidBookGenreException(String message) {
        super(message);
    }
}