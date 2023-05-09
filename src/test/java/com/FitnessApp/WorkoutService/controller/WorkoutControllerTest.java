package com.FitnessApp.WorkoutService.controller;

import com.FitnessApp.WorkoutService.business.enums.Difficulty;
import com.FitnessApp.WorkoutService.business.enums.ExerciseType;
import com.FitnessApp.WorkoutService.business.enums.WorkoutType;
import com.FitnessApp.WorkoutService.business.handler.exception.ValidationException;
import com.FitnessApp.WorkoutService.business.repository.model.WorkoutEntity;
import com.FitnessApp.WorkoutService.business.service.WorkoutService;
import com.FitnessApp.WorkoutService.model.ExerciseDto;
import com.FitnessApp.WorkoutService.model.WorkoutCreationDto;
import com.FitnessApp.WorkoutService.model.WorkoutDto;
import com.FitnessApp.WorkoutService.web.controller.WorkoutController;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class WorkoutControllerTest {
    @Mock
    WorkoutService workoutService;

    @InjectMocks
    WorkoutController workoutController;
    WorkoutEntity entity;
    WorkoutEntity entity2;
    WorkoutDto dto;
    WorkoutDto dto2;
    WorkoutCreationDto creationDto;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        entity = new WorkoutEntity("id", "workout", LocalDate.now(), Difficulty.Begginer, "user", WorkoutType.Strength);
        entity2 = new WorkoutEntity("id2", "workout2", LocalDate.now(), Difficulty.Medium, "user", WorkoutType.Strength);

        dto = new WorkoutDto("id", "workout", LocalDate.now(), Difficulty.Begginer, "user", WorkoutType.Strength);
        dto2 = new WorkoutDto("id2", "workout2", LocalDate.now(), Difficulty.Medium, "user", WorkoutType.Strength);
        List<ExerciseDto> exercises = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            exercises.add(new ExerciseDto(String.valueOf(i),
                    "exercise" + i,
                    "description" + i,
                    i,
                    i,
                    null,
                    ExerciseType.Strength));
        }
        creationDto = new WorkoutCreationDto("workout", LocalDate.now(), exercises, Difficulty.Begginer, WorkoutType.Strength);

    }

    @Test
    void findWorkoutByIdSuccess() {
        Mockito.when(workoutService.findWorkoutById(any())).thenReturn(dto);
        ResponseEntity<WorkoutDto> result = workoutController.findWorkoutById("id");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void findWorkoutByIdNotFound() {
        Mockito.when(workoutService.findWorkoutById(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> workoutController.findWorkoutById("id"));
    }

    @Test
    void findAllWorkoutsFound() {
        List<WorkoutDto> expected = List.of(dto, dto2);
        Mockito.when(workoutService.findAllWorkouts()).thenReturn(expected);
        ResponseEntity<List<WorkoutDto>> result = workoutController.findAllWorkouts();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(expected.containsAll(result.getBody()) && result.getBody().containsAll(expected));
    }

    @Test
    void findAllWorkoutsEmptyList() {
        List<WorkoutDto> expected = Collections.emptyList();
        Mockito.when(workoutService.findAllWorkouts()).thenReturn(expected);
        ResponseEntity<List<WorkoutDto>> result = workoutController.findAllWorkouts();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void createWorkoutSuccess() throws JsonProcessingException {
        Mockito.when(workoutService.createWorkout(any())).thenReturn(dto);
        ResponseEntity<WorkoutDto> result = workoutController.createWorkout(creationDto,
                new MapBindingResult(Collections.emptyMap(), "errors"));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void createWorkoutInvalidData() throws JsonProcessingException {
        MapBindingResult mapBindingResult = new MapBindingResult(Collections.emptyMap(), "errors");
        mapBindingResult.addError(new ObjectError("field", "invalid data"));
        assertThrows(ValidationException.class, () -> workoutController.createWorkout(creationDto, mapBindingResult));
    }

    @Test
    void updateWorkoutSuccess() {
        Mockito.when(workoutService.updateWorkoutById(any(), any())).thenReturn(dto);
        ResponseEntity<WorkoutDto> result = workoutController.updateWorkout("id", dto,
                new MapBindingResult(Collections.emptyMap(), "errors"));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void updateWorkoutInvalidData() {
        MapBindingResult mapBindingResult = new MapBindingResult(Collections.emptyMap(), "errors");
        mapBindingResult.addError(new ObjectError("field", "invalid data"));
        assertThrows(ValidationException.class, () -> workoutController.updateWorkout("id", dto, mapBindingResult));
    }

    @Test
    void updateWorkoutNotFound() {
        Mockito.when(workoutService.updateWorkoutById(any(), any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> workoutController.updateWorkout("id", dto,
                new MapBindingResult(Collections.emptyMap(), "errors")));

    }

    @Test
    void deleteWorkout() {
        doNothing().when(workoutService).deleteWorkoutById(any());
        ResponseEntity<Void> result = workoutController.deleteWorkout("id");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.hasBody());
    }

    @Test
    void deleteWorkoutNotFound() {
        doThrow(NoSuchElementException.class).when(workoutService).deleteWorkoutById(any());
        assertThrows(NoSuchElementException.class, () -> workoutController.deleteWorkout("id"));
    }
}