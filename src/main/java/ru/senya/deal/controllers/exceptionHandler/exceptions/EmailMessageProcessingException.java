package ru.senya.deal.controllers.exceptionHandler.exceptions;

public class EmailMessageProcessingException extends RuntimeException{
    public EmailMessageProcessingException(String msg) {
        super(msg);
    }
}
