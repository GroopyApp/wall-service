package app.groopy.roomservice.domain.exceptions;

import app.groopy.roomservice.domain.models.ListRoomRequestDto;

public class ListRoomException extends Throwable {

    public ListRoomException(ListRoomRequestDto request, String cause) {
        super(String.format("An error occurred trying to list rooms with given request: {%s}. \n exception: {%s}", request, cause));
    }
}
