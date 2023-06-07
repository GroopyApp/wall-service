package app.groopy.wallservice.infrastructure.repository;

import app.groopy.wallservice.infrastructure.models.TopicEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TopicRepository extends MongoRepository<TopicEntity, String> {

    default List<TopicEntity> findByCriteria(MongoTemplate mongoTemplate, String wallId, List<String> hashtags, List<String> languages, boolean onlyValidEvents) {
        Query query = new Query();
        query.addCriteria(Criteria.where("wallId").is(wallId));

        if (hashtags != null && !hashtags.isEmpty()) {
            query.addCriteria(Criteria.where("categories").all(hashtags));
        }

        if (languages != null && !languages.isEmpty()) {
            query.addCriteria(Criteria.where("language").in(languages));
        }

        List<TopicEntity> result = mongoTemplate.find(query, TopicEntity.class);

        if (onlyValidEvents) {
            var now = LocalDateTime.now();

            return result.stream()
                    .peek(topic -> topic.setEvents(topic.getEvents().stream()
                            .filter(eventEntity -> eventEntity.getStartDate().isAfter(now))
                            .toList()))
                    .toList();
        }
        return result;
    }
}
