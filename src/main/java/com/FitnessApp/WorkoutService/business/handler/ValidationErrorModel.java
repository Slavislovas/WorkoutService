package com.FitnessApp.WorkoutService.business.handler;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ValidationErrorModel {
    ArrayList<ValidationError> errors;

    public ValidationErrorModel(){
        errors = new ArrayList<>();
    }

    public void addValidationError(String field, String message){
        errors.add(new ValidationError(field, message));
    }
}
