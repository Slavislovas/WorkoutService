package com.FitnessApp.WorkoutService.integration;

import com.FitnessApp.WorkoutService.business.enums.Difficulty;
import com.FitnessApp.WorkoutService.business.enums.ExerciseType;
import com.FitnessApp.WorkoutService.business.enums.WorkoutType;
import com.FitnessApp.WorkoutService.business.handler.exception.ValidationException;
import com.FitnessApp.WorkoutService.business.repository.WorkoutRepository;
import com.FitnessApp.WorkoutService.business.repository.model.WorkoutEntity;
import com.FitnessApp.WorkoutService.model.ExerciseDto;
import com.FitnessApp.WorkoutService.model.WorkoutCreationDto;
import com.FitnessApp.WorkoutService.model.WorkoutDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class WorkoutIntegrationTest {

    @MockBean
    WorkoutRepository workoutRepository;

    @Autowired
    MockMvc mockMvc;

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

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void findWorkoutByIdSuccess() throws Exception {
        Mockito.when(workoutRepository.findById(any())).thenReturn(Optional.of(entity));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(dto);
        MvcResult mvcResult = mockMvc.perform(get("/workout/find/1L"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, mvcResult.getResponse().getContentAsString());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void findWorkoutByIdNotFound() throws Exception {
        Mockito.when(workoutRepository.findById(any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/workout/find/1L"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void findAllWorkoutsFound() throws Exception {
        Mockito.when(workoutRepository.findAll()).thenReturn(List.of(entity, entity2));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(List.of(dto, dto2));
        MvcResult result = mockMvc.perform(get("/workout/find/all"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void createWorkoutSuccess() throws Exception {
        Mockito.when(workoutRepository.save(any())).thenReturn(entity);
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(creationDto);
        MvcResult result = mockMvc
                .perform(post("/workout/create")
                        .content(expectedResult)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void createWorkoutInvalidData() throws Exception {
        creationDto.setTitle(null);
        Mockito.when(workoutRepository.save(any())).thenReturn(entity);
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(creationDto);
        MvcResult result = mockMvc
                .perform(post("/workout/create")
                        .content(expectedResult)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void updateWorkoutSuccess() throws Exception {
        Mockito.when(workoutRepository.save(any())).thenReturn(entity);
        Mockito.when(workoutRepository.findById(any())).thenReturn(Optional.ofNullable(entity));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(dto2);
        MvcResult result = mockMvc
                .perform(put("/workout/edit/1L")
                        .content(expectedResult)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void updateWorkoutInvalidData() throws Exception {
        dto2.setTitle(null);
        Mockito.when(workoutRepository.save(any())).thenReturn(entity);
        Mockito.when(workoutRepository.findById(any())).thenReturn(Optional.ofNullable(entity));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(dto2);
        MvcResult result = mockMvc
                .perform(put("/workout/edit/1L")
                        .content(expectedResult)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void updateWorkoutNotFound() throws Exception {
        Mockito.when(workoutRepository.findById(any())).thenReturn(Optional.empty());
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(dto2);
        MvcResult result = mockMvc
                .perform(put("/workout/edit/1L")
                        .content(expectedResult)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, result.getResponse().getContentAsString());
    }

    @WithMockUser(username = "testUser", roles = "USER")
    @Test
    void deleteWorkoutNotFound() throws Exception {
        Mockito.when(workoutRepository.findById(any())).thenReturn(Optional.empty());
        mockMvc
                .perform(delete("/workout/delete/1L"))
                .andExpect(status().isNotFound());
    }
}
