package ru.senya.deal.controllers.exceptionHandler.exceptions;

public class StatusHistoryProcessingException extends RuntimeException {
    public StatusHistoryProcessingException(String msg) {
        super(msg);
    }
}
