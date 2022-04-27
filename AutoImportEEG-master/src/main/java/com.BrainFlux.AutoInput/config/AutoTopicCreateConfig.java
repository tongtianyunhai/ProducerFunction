package com.BrainFlux.AutoInput.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoTopicCreateConfig {

    @Value("${topic}")
    private String topic;

    @Value("${partitions}")
    private Integer partitions;

    @Value("${replicas}")
    private Integer replicas;

    @Bean
    @Profile("localTest")
    public NewTopic ImportCSVEvents(){
        System.out.println("localTest");
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
