package com.FitnessApp.WorkoutService.model;

import com.FitnessApp.WorkoutService.business.enums.Difficulty;
import com.FitnessApp.WorkoutService.business.enums.WorkoutType;
import com.FitnessApp.WorkoutService.business.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutCreationDto {
    @NotNull
    @NotBlank
    @ApiModelProperty(name = "title", notes = "Title of the workout", example = "Massive muscle gains")
    private String title;

    @NotNull
    @JsonFormat(pattern = Constants.dateFormat)
    @ApiModelProperty(name = "creationDate", notes = "The date, when the workout was created", example = "2023-11-12")
    private LocalDate creationDate;

    @NotEmpty
    @ApiModelProperty(name = "exercises", notes = "List of exercises in this workout")
    private List<ExerciseDto> exercises;

    @NotNull
    @ApiModelProperty(name = "difficulty", notes = "Difficulty of the workout", example = "Beginner")
    private Difficulty difficulty;

    @NotNull
    @ApiModelProperty(name = "type", notes = "Type of the workout", example = "Strength")
    private WorkoutType type;
}
