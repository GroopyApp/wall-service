package app.groopy.roomservice.domain.validators;

import app.groopy.roomservice.domain.models.ListRoomInternalRequest;
import org.springframework.stereotype.Component;

@Component
public class ListRoomValidator {


    public void validate(ListRoomInternalRequest request) {
        //TODO add logic to decide if room must be created or not
    }
}
