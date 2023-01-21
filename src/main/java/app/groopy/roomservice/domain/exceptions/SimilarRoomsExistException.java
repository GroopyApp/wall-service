package app.groopy.roomservice.domain.exceptions;

import app.groopy.roomservice.domain.models.common.RoomDetailsDto;

import java.util.Arrays;
import java.util.List;

public class SimilarRoomsExistException extends Throwable {

    List<RoomDetailsDto> alternativeRooms;
    public SimilarRoomsExistException(List<String> hashtags, List<String> languages, List<RoomDetailsDto> rooms) {
        super(
                "Impossible to create room, similar rooms have been found in the system with following params: " +
                        "hashtags=" +
                        Arrays.toString(hashtags.toArray()) +
                        "languages=" +
                        Arrays.toString(languages.toArray())
        );
        this.alternativeRooms = rooms;
    }

    public List<RoomDetailsDto> getAlternativeRooms() {
        return alternativeRooms;
    }
}
