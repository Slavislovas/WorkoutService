package com.FitnessApp.WorkoutService.business.repository;

import com.FitnessApp.WorkoutService.business.repository.model.WorkoutEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends MongoRepository<WorkoutEntity, String> {
}
