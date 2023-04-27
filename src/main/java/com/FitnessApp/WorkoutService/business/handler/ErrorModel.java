package com.FitnessApp.WorkoutService.business.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    private int statusCode;
    private String message;
    private LocalDate timeStamp;
    private String path;
}
