package ru.senya.deal.controller.exceptionHandler.exceptions;

public class LoanOfferProcessingException extends RuntimeException {
    public LoanOfferProcessingException(String msg) {
        super(msg);
    }
}
