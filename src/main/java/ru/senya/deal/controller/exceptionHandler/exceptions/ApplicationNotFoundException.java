package ru.senya.deal.controller.exceptionHandler.exceptions;

public class ApplicationNotFoundException extends RuntimeException{

    public ApplicationNotFoundException(String message) {
        super(message);
    }

}
