package com.FitnessApp.WorkoutService.business.service;

import com.FitnessApp.WorkoutService.model.WorkoutCreationDto;
import com.FitnessApp.WorkoutService.model.WorkoutDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface WorkoutService {
    WorkoutDto findWorkoutById(String workoutId);
    List<WorkoutDto> findAllWorkouts();
    WorkoutDto createWorkout(WorkoutCreationDto workoutCreationDto) throws JsonProcessingException;

    WorkoutDto updateWorkoutById(String workoutId, WorkoutDto workoutData);
}
