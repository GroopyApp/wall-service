package app.groopy.wallservice.domain.services;

import app.groopy.wallservice.domain.exceptions.EndDateIsBeforeException;
import app.groopy.wallservice.domain.exceptions.EntityAlreadyExistsException;
import app.groopy.wallservice.domain.exceptions.EventInThePastException;
import app.groopy.wallservice.domain.exceptions.RequiredParameterException;
import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.domain.models.requests.CreateEventRequestDto;
import app.groopy.wallservice.infrastructure.models.Entity;
import app.groopy.wallservice.infrastructure.models.EventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public void validate(CreateEventRequestDto createEventRequest) throws EndDateIsBeforeException, EventInThePastException {
        if (createEventRequest.getEndDate().isBefore(createEventRequest.getStartDate())) {
            throw new EndDateIsBeforeException(createEventRequest.getStartDate(), createEventRequest.getEndDate());
        }
        if (createEventRequest.getStartDate().isBefore(LocalDateTime.now())) {
            throw new EventInThePastException(createEventRequest.getStartDate());
        }
    }

    public void validateSearch(SearchCriteriaDto requestCriteria) throws RequiredParameterException {
        if (requestCriteria.getLocation() == null) {
            throw new RequiredParameterException("location");
        }
    }

    public void validateUserSearch(SearchCriteriaDto requestCriteria) throws RequiredParameterException {
        if (requestCriteria.getUserId() == null || requestCriteria.getUserId().isEmpty()) {
            throw new RequiredParameterException("userId");
        }
        validateSearch(requestCriteria);
    }
}
