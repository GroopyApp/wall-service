package app.funfinder.roomservice.domain.processors;

import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import app.funfinder.roomservice.domain.models.CreateRoomInternalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateRoomProcessor implements Processor<CreateRoomInternalRequest, CreateRoomInternalResponse> {


    @Override
    public CreateRoomInternalResponse process(CreateRoomInternalRequest input) {
        return null;
    }
}
