package com.example.demo.exceptions;

public class IncorrectInputException extends RuntimeException {
    public IncorrectInputException(String message) {
        super(message);
    }
}
