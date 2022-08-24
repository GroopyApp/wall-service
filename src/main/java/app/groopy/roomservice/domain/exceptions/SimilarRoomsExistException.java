package app.groopy.roomservice.domain.exceptions;

import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.domain.models.common.RoomLocation;

import java.util.Arrays;
import java.util.List;

public class SimilarRoomsExistException extends Throwable {

    List<RoomDetails> alternativeRooms;
    public SimilarRoomsExistException(List<String> hashtags, List<String> languages, List<RoomDetails> rooms) {
        super(
                "Impossible to create room, similar rooms have been found in the system with following params: " +
                        "hashtags=" +
                        Arrays.toString(hashtags.toArray()) +
                        "languages=" +
                        Arrays.toString(languages.toArray())
        );
        this.alternativeRooms = rooms;
    }

    public List<RoomDetails> getAlternativeRooms() {
        return alternativeRooms;
    }
}
