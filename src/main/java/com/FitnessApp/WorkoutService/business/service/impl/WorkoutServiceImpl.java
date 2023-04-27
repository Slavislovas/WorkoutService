package com.FitnessApp.WorkoutService.business.service.impl;

import com.FitnessApp.WorkoutService.business.mapper.WorkoutMapStruct;
import com.FitnessApp.WorkoutService.business.repository.WorkoutRepository;
import com.FitnessApp.WorkoutService.business.repository.model.WorkoutEntity;
import com.FitnessApp.WorkoutService.business.service.WorkoutService;
import com.FitnessApp.WorkoutService.model.ExerciseDto;
import com.FitnessApp.WorkoutService.model.WorkoutCreationDto;
import com.FitnessApp.WorkoutService.model.WorkoutDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WorkoutServiceImpl implements WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapStruct workoutMapStruct;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public WorkoutDto findWorkoutById(String workoutId) {
        return null;
    }

    @Override
    public List<WorkoutDto> findAllWorkouts() {
        return null;
    }

    @Override
    public WorkoutDto createWorkout(WorkoutCreationDto workoutCreationDto) throws JsonProcessingException {
        WorkoutEntity workoutEntity = workoutMapStruct.creationDtoToEntity(workoutCreationDto);
        workoutEntity.setCreatorUsername((String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
        List<ExerciseDto> exerciseDtos = workoutCreationDto.getExercises();
        WorkoutEntity createdEntity = workoutRepository.save(workoutEntity);
        for (ExerciseDto exercise: exerciseDtos) {
            exercise.setWorkoutId(createdEntity.getId());
            kafkaTemplate.send("workout-exercise", exercise);
        }
        return workoutMapStruct.entityToDto(createdEntity);
    }

    @Override
    public WorkoutDto updateWorkoutById(String workoutId, WorkoutDto workoutData) {
        Optional<WorkoutEntity> optionalWorkoutEntity = workoutRepository.findById(workoutId);
        if (optionalWorkoutEntity.isEmpty()){
            throw new NoSuchElementException("Workout with id: " + workoutId + " does not exist");
        }
        WorkoutEntity workoutEntity = workoutMapStruct.dtoToEntity(workoutData);
        workoutEntity.setId(workoutId);
        workoutEntity.setCreatorUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return workoutMapStruct.entityToDto(workoutRepository.save(workoutEntity));
    }
}
