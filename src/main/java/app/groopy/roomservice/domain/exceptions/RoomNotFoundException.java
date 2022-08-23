package app.groopy.roomservice.domain.exceptions;

public class RoomNotFoundException extends Throwable {

    public RoomNotFoundException(String userId) {
        super("room not found with id: " + userId);
    }
}
