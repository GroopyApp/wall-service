package app.groopy.wallservice.domain.exceptions;

import app.groopy.wallservice.infrastructure.models.TopicEntity;
import app.groopy.wallservice.infrastructure.models.WallEntity;
import lombok.Getter;

@Getter
public class TopicNotFoundException extends Exception {

    private final String id;
    private final String entityName = TopicEntity.class.getSimpleName();

    public TopicNotFoundException(String id) {
        super(String.format("Topic not found for id %s", id));
        this.id = id;
    }
}
