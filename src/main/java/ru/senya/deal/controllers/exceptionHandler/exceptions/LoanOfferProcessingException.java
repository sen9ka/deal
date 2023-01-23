package ru.senya.deal.controllers.exceptionHandler.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LoanOfferProcessingException extends JsonProcessingException {
    public LoanOfferProcessingException(String msg) {
        super(msg);
    }
}
