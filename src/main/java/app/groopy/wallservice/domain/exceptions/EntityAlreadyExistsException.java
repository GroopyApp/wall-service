package app.groopy.wallservice.domain.exceptions;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends Exception {

    private final String entityName;
    private final String id;

    public EntityAlreadyExistsException(Class<?> entity, String id) {
        super(String.format("A similar or equal entity %s already exists: id %s", entity.getSimpleName(), id));
        this.entityName = entity.getSimpleName();
        this.id = id;
    }
}
