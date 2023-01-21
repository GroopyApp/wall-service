package app.groopy.roomservice.domain.exceptions;

public class SubscribeRoomException extends Throwable {

    public SubscribeRoomException(String userId, String roomId, String cause) {
        super(String.format("An error occurred trying to subscribe user %s to room %s. \n exception: {%s}", userId, roomId, cause));
    }
}
