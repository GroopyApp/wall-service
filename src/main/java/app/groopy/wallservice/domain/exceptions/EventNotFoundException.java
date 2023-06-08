package app.groopy.wallservice.domain.exceptions;

import app.groopy.wallservice.infrastructure.models.EventEntity;
import app.groopy.wallservice.infrastructure.models.TopicEntity;
import lombok.Getter;

@Getter
public class EventNotFoundException extends Exception {

    private final String id;
    private final String entityName = EventEntity.class.getSimpleName();

    public EventNotFoundException(String id) {
        super(String.format("Event not found for id %s", id));
        this.id = id;
    }
}
