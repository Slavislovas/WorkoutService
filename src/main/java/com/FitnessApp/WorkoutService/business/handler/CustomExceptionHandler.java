package com.FitnessApp.WorkoutService.business.handler;

import com.FitnessApp.WorkoutService.business.handler.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorModel> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(404, ex.getMessage(),
                LocalDate.now(),
                request.getRequestURI());
        return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorModel> handleValidationException(ValidationException ex){
        return new ResponseEntity<>(ex.getValidationErrorModel(), HttpStatus.BAD_REQUEST);
    }
}
