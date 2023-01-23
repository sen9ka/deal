package ru.senya.deal.controllers.exceptionHandler.exceptions;

public class ApplicationNotFoundException extends RuntimeException{

    public ApplicationNotFoundException(String message) {
        super(message);
    }

}
