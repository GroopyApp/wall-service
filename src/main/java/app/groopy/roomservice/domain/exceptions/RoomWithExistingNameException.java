package app.groopy.roomservice.domain.exceptions;

public class RoomWithExistingNameException extends Throwable {

    public RoomWithExistingNameException(String value) {
        super("Room with name " + value + " already exists");
    }

}
