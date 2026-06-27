package com.wferdinando.poc_park_api.exception;

public class CodigoUniqueViolationException extends RuntimeException {

    public CodigoUniqueViolationException(String message) {
        super(message);
    }
}
