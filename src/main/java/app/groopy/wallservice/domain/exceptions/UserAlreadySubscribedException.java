package app.groopy.wallservice.domain.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadySubscribedException extends Exception {

    private final String targetId;
    private final String userId;

    public UserAlreadySubscribedException(String userId, String targetId, SubscriptionType type) {
        super(String.format("User with id %s is already subscribed to %s with id %s", userId, targetId, type.getType()));
        this.targetId = targetId;
        this.userId = userId;
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
