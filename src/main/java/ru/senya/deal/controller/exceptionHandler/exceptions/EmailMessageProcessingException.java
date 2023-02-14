package ru.senya.deal.controller.exceptionHandler.exceptions;

public class EmailMessageProcessingException extends RuntimeException{
    public EmailMessageProcessingException(String msg) {
        super(msg);
    }
}
