package app.funfinder.roomservice.domain.validators;

import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateRoomValidator {


    public void validate(CreateRoomInternalRequest request) {
        //TODO add logic to decide if room must be created or not
    }
}
