package app.groopy.roomservice.domain.exceptions;

public class SubscribeToRoomException extends Throwable {

    private final String userId;
    private final String roomId;

    public SubscribeToRoomException(String userId, String roomId) {
        super(String.format("An error occurred subscribing user with id %s to room %s", userId, roomId));
        this.userId = userId;
        this.roomId = roomId;
    }
}
