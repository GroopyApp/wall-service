package app.groopy.wallservice.domain.exceptions;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends Exception {

    private final String entityName;
    private final String id;

    public EntityAlreadyExistsException(Class<?> entity, String id) {
        super(String.format("An entity %s already exists with id %s", entity.getSimpleName(), id));
        this.entityName = entity.getSimpleName();
        this.id = id;
    }
}
