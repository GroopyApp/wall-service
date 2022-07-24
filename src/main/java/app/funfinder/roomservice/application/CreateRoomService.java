package app.funfinder.roomservice.application;

import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import app.funfinder.roomservice.domain.models.CreateRoomInternalResponse;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateRoomService {

    private final Logger logger = LoggerFactory.getLogger(CreateRoomService.class);

    public CreateRoomInternalResponse createRoom(CreateRoomInternalRequest request) {
        logger.info("Logic to create a room, including decision based on location, user, languages and hashtags");
        return CreateRoomInternalResponse.builder()
                .responseStatus(Status.CREATED)
                .room(RoomDetails.builder().roomId("12345A").build())
                .build();
    }
}
