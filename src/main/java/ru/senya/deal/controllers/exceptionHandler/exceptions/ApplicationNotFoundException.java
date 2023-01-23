package ru.senya.deal.controllers.exceptionHandler.exceptions;
import jakarta.persistence.EntityNotFoundException;

public class ApplicationNotFoundException extends EntityNotFoundException{

    public ApplicationNotFoundException(String message) {
        super(message);
    }

}
