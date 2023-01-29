package ru.senya.deal.controllers.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.senya.deal.controllers.exceptionHandler.exceptions.ApplicationNotFoundException;
import ru.senya.deal.controllers.exceptionHandler.exceptions.LoanOfferProcessingException;
import ru.senya.deal.controllers.exceptionHandler.exceptions.StatusHistoryProcessingException;

@ControllerAdvice
public class ApplicationExceptionHandler {

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

}
