package app.groopy.roomservice.domain.exceptions;

import app.groopy.roomservice.domain.models.ListRoomRequestDto;

public class ListUserRoomException extends Throwable {

    public ListUserRoomException(String userId, String cause) {
        super(String.format("An error occurred trying to list rooms for a given user: {%s}. \n exception: {%s}", userId, cause));
    }
}
