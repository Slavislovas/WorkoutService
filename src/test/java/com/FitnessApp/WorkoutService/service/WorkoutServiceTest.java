package com.FitnessApp.WorkoutService.service;

import com.FitnessApp.WorkoutService.business.enums.Difficulty;
import com.FitnessApp.WorkoutService.business.enums.ExerciseType;
import com.FitnessApp.WorkoutService.business.enums.WorkoutType;
import com.FitnessApp.WorkoutService.business.mapper.WorkoutMapStruct;
import com.FitnessApp.WorkoutService.business.repository.WorkoutRepository;
import com.FitnessApp.WorkoutService.business.repository.model.WorkoutEntity;
import com.FitnessApp.WorkoutService.business.service.impl.WorkoutServiceImpl;
import com.FitnessApp.WorkoutService.model.ExerciseDto;
import com.FitnessApp.WorkoutService.model.WorkoutCreationDto;
import com.FitnessApp.WorkoutService.model.WorkoutDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {
    @Mock
    WorkoutRepository workoutRepository;

    @Mock
    WorkoutMapStruct workoutMapStruct;

    @Mock
    KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    WorkoutServiceImpl workoutService;

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
        when(workoutRepository.findById(any())).thenReturn(Optional.of(entity));
        when(workoutMapStruct.entityToDto(entity)).thenReturn(dto);
        WorkoutDto result = workoutService.findWorkoutById("1");
        assertEquals(dto, result);
    }

    @Test
    void findWorkoutByIdNotFound() {
        when(workoutRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> workoutService.findWorkoutById("1"));
    }

    @Test
    void findAllWorkoutsSuccess() {
        when(workoutRepository.findAll()).thenReturn(List.of(entity, entity2));
        when(workoutMapStruct.entityToDto(entity)).thenReturn(dto);
        when(workoutMapStruct.entityToDto(entity2)).thenReturn(dto2);

        List<WorkoutDto> expected = List.of(dto, dto2);
        List<WorkoutDto> result = workoutService.findAllWorkouts();
        assertTrue(expected.containsAll(result) && result.containsAll(expected));
    }

    @Test
    void findAllWorkoutsEmptyList() {
        when(workoutRepository.findAll()).thenReturn(Collections.emptyList());
        List<WorkoutDto> result = workoutService.findAllWorkouts();
        assertTrue(result.isEmpty());
    }

    @Test
    void createWorkoutSuccess() throws JsonProcessingException {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn("username");

        when(workoutRepository.save(any())).thenReturn(entity);
        when(workoutMapStruct.creationDtoToEntity(creationDto)).thenReturn(entity);
        when(workoutMapStruct.entityToDto(entity)).thenReturn(dto);
        WorkoutDto result = workoutService.createWorkout(creationDto);
        assertEquals(dto, result);
    }

    @Test
    void updateWorkoutByIdSuccess() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn("username");

        when(workoutRepository.findById(any())).thenReturn(Optional.ofNullable(entity));
        when(workoutMapStruct.dtoToEntity(dto)).thenReturn(entity);
        when(workoutRepository.save(any())).thenReturn(entity);
        when(workoutMapStruct.entityToDto(any())).thenReturn(dto);
        WorkoutDto result = workoutService.updateWorkoutById("1", dto);
        assertEquals(dto, result);
    }

    @Test
    void deleteWorkoutByIdSuccess() {
        when(workoutRepository.findById(any())).thenReturn(Optional.ofNullable(entity));
        doNothing().when(workoutRepository).deleteById(any());
        workoutService.deleteWorkoutById("1");
    }

    @Test
    void deleteWorkoutNotFound() {
        when(workoutRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> workoutService.deleteWorkoutById("a"));
    }
}