package app.groopy.wallservice.domain.exceptions;

import app.groopy.wallservice.infrastructure.models.UserEntity;
import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {

    private final String id;
    private final String entityName = UserEntity.class.getSimpleName();

    public UserNotFoundException(String id) {
        super(String.format("User not found for id %s", id));
        this.id = id;
    }
}
