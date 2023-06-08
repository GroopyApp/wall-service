package app.groopy.wallservice.infrastructure.repository;

import app.groopy.wallservice.infrastructure.models.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, String> {
    List<EventEntity> findByTopicAndStartDateAfter(String topicId, LocalDateTime now);
}
