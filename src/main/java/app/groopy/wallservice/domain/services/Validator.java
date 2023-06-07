package app.groopy.wallservice.domain.services;

import app.groopy.wallservice.domain.exceptions.EntityAlreadyExistsException;
import app.groopy.wallservice.infrastructure.models.Entity;
import app.groopy.wallservice.infrastructure.models.EventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class Validator {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public Validator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public <T extends Entity> void validate(String identifier, Class<T> clazz) throws EntityAlreadyExistsException {
        Query query = new Query();
        query.addCriteria(Criteria.where("chatId").is(identifier));
        T result = mongoTemplate.findOne(query, clazz);
        if (result != null) {
            throw new EntityAlreadyExistsException(clazz, result.getId());
        }
    }

    public void validate(String identifier, List<EventEntity> events) throws EntityAlreadyExistsException {
        Optional<EventEntity> eventEntity = events.stream()
                .filter(event -> event.getIdentifier().equals(identifier))
                .findFirst();
        if (eventEntity.isPresent()) {
            throw new EntityAlreadyExistsException(EventEntity.class, eventEntity.get().getIdentifier());
        }
    }
}
