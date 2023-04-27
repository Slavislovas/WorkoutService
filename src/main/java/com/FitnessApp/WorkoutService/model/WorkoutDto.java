package com.FitnessApp.WorkoutService.model;

import com.FitnessApp.WorkoutService.business.enums.Difficulty;
import com.FitnessApp.WorkoutService.business.enums.WorkoutType;
import com.FitnessApp.WorkoutService.business.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "Workout data transfer object")
public class WorkoutDto {
    @ApiModelProperty(name = "id", notes = "Id of the workout", example = "123AAds", hidden = true)
    private String id;

    @NotNull
    @NotBlank
    @ApiModelProperty(name = "title", notes = "Title of the workout", example = "Massive muscle gains")
    private String title;

    @NotNull
    @JsonFormat(pattern = Constants.dateFormat)
    @ApiModelProperty(name = "creationDate", notes = "The date, when the workout was created", example = "2023-11-12")
    private LocalDate creationDate;

    @NotNull
    @ApiModelProperty(name = "difficulty", notes = "Difficulty of the workout", example = "Beginner")
    private Difficulty difficulty;

    @ApiModelProperty(name = "creatorUsername", notes = "Username of the creator of this workout", example = "PersonalTrainer123")
    private String creatorUsername;

    @NotNull
    @ApiModelProperty(name = "type", notes = "Type of the workout", example = "Strength")
    private WorkoutType type;
}
