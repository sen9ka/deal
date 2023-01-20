package ru.senya.deal.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Заявка не найдена")
    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<?> handleApplicationNotFoundException(ApplicationNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
