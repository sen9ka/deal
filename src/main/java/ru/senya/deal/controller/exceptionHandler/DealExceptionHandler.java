package ru.senya.deal.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.senya.deal.controller.exceptionHandler.exceptions.*;

@ControllerAdvice
public class DealExceptionHandler {

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<Object> handleApplicationNotFoundException(ApplicationNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanOfferProcessingException.class)
    public ResponseEntity<Object> handleLoanOfferProcessingException(LoanOfferProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StatusHistoryProcessingException.class)
    public ResponseEntity<Object> handleStatusHistoryProcessingException(StatusHistoryProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailMessageProcessingException.class)
    public ResponseEntity<Object> handleEmailMessageProcessingException(EmailMessageProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentScheduleProcessingException.class)
    public ResponseEntity<Object> handlePaymentScheduleProcessingException(EmailMessageProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

}
