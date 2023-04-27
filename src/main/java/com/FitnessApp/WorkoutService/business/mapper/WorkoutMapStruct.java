package com.FitnessApp.WorkoutService.business.mapper;

import com.FitnessApp.WorkoutService.business.repository.model.WorkoutEntity;
import com.FitnessApp.WorkoutService.model.WorkoutCreationDto;
import com.FitnessApp.WorkoutService.model.WorkoutDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkoutMapStruct {
    WorkoutDto entityToDto(WorkoutEntity entity);
    WorkoutEntity creationDtoToEntity(WorkoutCreationDto creationDto);
    WorkoutEntity dtoToEntity(WorkoutDto dto);
}
