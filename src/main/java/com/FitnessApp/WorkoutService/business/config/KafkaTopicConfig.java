package com.FitnessApp.WorkoutService.business.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic fitnessAppTopic(){
        return TopicBuilder.name("workout-exercise").build();
    }

    @Bean
    public NewTopic deleteWorkoutTopic(){
        return TopicBuilder.name("workout-exercise-delete").build();
    }
}
