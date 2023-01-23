package ru.senya.deal.controllers.exceptionHandler.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class StatusHistoryProcessingException extends JsonProcessingException {
    public StatusHistoryProcessingException(String msg) {
        super(msg);
    }
}
