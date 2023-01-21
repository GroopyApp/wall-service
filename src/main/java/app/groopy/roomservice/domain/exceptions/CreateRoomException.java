package app.groopy.roomservice.domain.exceptions;

import app.groopy.roomservice.domain.models.CreateRoomRequestDto;

public class CreateRoomException extends Throwable {

    public CreateRoomException(CreateRoomRequestDto request, String cause) {
        super(String.format("An error occurred trying to create a room with given request: {%s}. \n exception: {%s}", request, cause));
    }
}
