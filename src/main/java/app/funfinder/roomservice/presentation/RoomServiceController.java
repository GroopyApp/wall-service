package app.funfinder.roomservice.presentation;

import app.funfinder.roomservice.application.CreateRoomService;
import app.funfinder.roomservice.application.ListRoomService;
import app.funfinder.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.funfinder.roomservice.presentation.mapper.PresentationMapper;
import app.funfinder.protobuf.RoomServiceProto;
import app.funfinder.roomservice.domain.models.common.SearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class RoomServiceController {

    private final Logger LOGGER = LoggerFactory.getLogger(RoomServiceController.class);
    @Autowired
    private CreateRoomService createRoomService;
    @Autowired
    private ListRoomService listRoomService;

    @Autowired
    private PresentationMapper presentationMapper;

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomServiceProto.CreateRoomResponse> createRoom(@RequestBody RoomServiceProto.CreateRoomRequest payload) throws CreateRoomValuesValidationException {
        LOGGER.info("Processing message {}", payload);
        RoomServiceProto.CreateRoomResponse response = presentationMapper.map(
                createRoomService.createRoom(presentationMapper.map(payload))
        );
        LOGGER.info("Sending CreateRoomResponse {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/list",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomServiceProto.ListRoomResponse> listRoom(@RequestBody RoomServiceProto.ListRoomRequest payload) {
        LOGGER.info("Processing message {}", payload);
        RoomServiceProto.ListRoomResponse response = presentationMapper.map(
                listRoomService.listRoom(presentationMapper.map(payload), SearchScope.STANDARD_SEARCH)
        );
        LOGGER.info("Sending ListRoomResponse {}", response);
        return ResponseEntity.ok(response);
    }
}
