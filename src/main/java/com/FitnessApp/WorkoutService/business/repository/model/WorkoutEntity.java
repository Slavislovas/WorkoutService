package com.FitnessApp.WorkoutService.business.repository.model;

import com.FitnessApp.WorkoutService.business.enums.Difficulty;
import com.FitnessApp.WorkoutService.business.enums.WorkoutType;
import com.FitnessApp.WorkoutService.model.ExerciseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("workout")
public class WorkoutEntity {
    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("creationDate")
    private LocalDate creationDate;

    @Field("difficulty")
    private Difficulty difficulty;

    @Field("creatorUsername")
    private String creatorUsername;

    @Field("type")
    private WorkoutType type;
}
