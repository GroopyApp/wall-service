package app.funfinder.roomservice.config;

import app.funfinder.roomservice.domain.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaConfig {

    @Bean
    public NewTopic roomServiceCreateTopic() {
        return TopicBuilder.name(KafkaTopics.ROOM_SERVICE_CREATE_TOPIC).build();
    }

    //TODO add delete
}