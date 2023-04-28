package com.FitnessApp.WorkoutService.web.controller;

import com.FitnessApp.WorkoutService.business.handler.ValidationErrorModel;
import com.FitnessApp.WorkoutService.business.handler.exception.ValidationException;
import com.FitnessApp.WorkoutService.business.service.WorkoutService;
import com.FitnessApp.WorkoutService.model.WorkoutCreationDto;
import com.FitnessApp.WorkoutService.model.WorkoutDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/workout")
@RequiredArgsConstructor
@RestController
public class WorkoutController {
    private final WorkoutService workoutService;

    @ApiOperation(value = "Finds workout by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 404, message = "Workout not found"),
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<WorkoutDto> findWorkoutById(@PathVariable("id") String workoutId){
        return ResponseEntity.ok(workoutService.findWorkoutById(workoutId));
    }

    @ApiOperation(value = "Finds all workouts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
    })
    @GetMapping("/find/all")
    public ResponseEntity<List<WorkoutDto>> findAllWorkouts(){
        return ResponseEntity.ok(workoutService.findAllWorkouts());
    }

    @ApiOperation(value = "Creates a new workout")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 400, message = "Bad request"),
    })
    @PostMapping("/create")
    public ResponseEntity<WorkoutDto> createWorkout(@Valid @RequestBody WorkoutCreationDto workoutCreationDto,
                                                    BindingResult bindingResult) throws JsonProcessingException {
        validateFields(bindingResult);
        return ResponseEntity.ok(workoutService.createWorkout(workoutCreationDto));
    }

    @ApiOperation(value = "Edits an existing workout's data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Workout not found")
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<WorkoutDto> updateWorkout(@PathVariable("id") String workoutId,
                                                    @Valid @RequestBody WorkoutDto workoutData,
                                                    BindingResult bindingResult){
        validateFields(bindingResult);
        return ResponseEntity.ok(workoutService.updateWorkoutById(workoutId, workoutData));
    }

    @ApiOperation(value = "Deletes workout and its exercises")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 404, message = "Workout not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable("id") String workoutId){
        workoutService.deleteWorkoutById(workoutId);
        return ResponseEntity.ok().build();
    }

    private static void validateFields(BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            ValidationErrorModel validationErrorModel = new ValidationErrorModel();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                validationErrorModel.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
            }
            throw new ValidationException(validationErrorModel);
        }
    }
}
