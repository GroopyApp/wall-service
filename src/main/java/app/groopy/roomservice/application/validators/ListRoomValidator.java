package app.groopy.roomservice.application.validators;

import app.groopy.roomservice.domain.models.ListRoomRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ListRoomValidator {


    public void validate(ListRoomRequestDto request) {
        //TODO add logic to decide if room must be created or not
    }

    public void validate(String userId) {
        //TODO add logic to decide if a user is valid or not
    }
}
