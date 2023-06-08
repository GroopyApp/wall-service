package app.groopy.wallservice.domain.exceptions;

import app.groopy.wallservice.infrastructure.models.WallEntity;
import lombok.Getter;

@Getter
public class UserAlreadySubscribedException extends Exception {

    private final String id;
    private final String entityName = WallEntity.class.getSimpleName();

    public UserAlreadySubscribedException(String userId, String id, SubscriptionType type) {
        super(String.format("User with id %s is already subscribed to %s with id %s", userId, id, type.getType()));
        this.id = id;
    }

    public enum SubscriptionType {

        TOPIC("topic"),
        EVENT("event");

        @Getter
        private String type;

        SubscriptionType(String type) {
            this.type = type;
        }
    }
}
