package ru.senya.deal.controller.exceptionHandler.exceptions;

public class PaymentScheduleProcessingException extends RuntimeException{

    public PaymentScheduleProcessingException(String msg) {
        super(msg);
    }

}
