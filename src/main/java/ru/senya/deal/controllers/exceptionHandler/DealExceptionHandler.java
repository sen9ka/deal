package ru.senya.deal.controllers.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.senya.deal.controllers.exceptionHandler.exceptions.*;

@ControllerAdvice
public class DealExceptionHandler {

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<?> handleApplicationNotFoundException(ApplicationNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanOfferProcessingException.class)
    public ResponseEntity<?> handleLoanOfferProcessingException(LoanOfferProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StatusHistoryProcessingException.class)
    public ResponseEntity<?> handleStatusHistoryProcessingException(StatusHistoryProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailMessageProcessingException.class)
    public ResponseEntity<?> handleEmailMessageProcessingException(EmailMessageProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentScheduleProcessingException.class)
    public ResponseEntity<?> handlePaymentScheduleProcessingException(EmailMessageProcessingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

}
