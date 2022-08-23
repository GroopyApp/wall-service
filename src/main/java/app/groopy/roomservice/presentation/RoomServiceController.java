package app.groopy.roomservice.presentation;

import app.groopy.roomservice.application.CreateRoomService;
import app.groopy.roomservice.application.ListRoomService;
import app.groopy.roomservice.application.SubscribeService;
import app.groopy.roomservice.domain.models.common.GeneralStatus;
import app.groopy.roomservice.presentation.mapper.PresentationMapper;
import app.groopy.protobuf.RoomServiceProto;
import app.groopy.roomservice.domain.models.common.SearchScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public class RoomServiceController {

    private final Logger LOGGER = LoggerFactory.getLogger(RoomServiceController.class);
    @Autowired
    private CreateRoomService createRoomService;
    @Autowired
    private ListRoomService listRoomService;
    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private PresentationMapper presentationMapper;

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomServiceProto.CreateRoomResponse> createRoom(@RequestBody RoomServiceProto.CreateRoomRequest payload) {
        LOGGER.info("Processing message {}", payload);
        RoomServiceProto.CreateRoomResponse response = presentationMapper.map(
                createRoomService.createRoom(presentationMapper.map(payload))
        );
        LOGGER.info("Sending CreateRoomResponse {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomServiceProto.ListRoomResponse> searchRooms(@RequestBody RoomServiceProto.ListRoomRequest payload) {
        LOGGER.info("Processing message {}", payload);
        RoomServiceProto.ListRoomResponse response = presentationMapper.map(
                listRoomService.listRoom(presentationMapper.map(payload), SearchScope.STANDARD_SEARCH)
        );
        LOGGER.info("Sending ListRoomResponse {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/subscribe",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomServiceProto.SubscribeRoomResponse> subscribe(@RequestBody RoomServiceProto.SubscribeRoomRequest payload) {
        LOGGER.info("Processing message {}", payload);
        subscribeService.subscribe(payload.getUserId(), payload.getRoomId());
        return ResponseEntity.ok(RoomServiceProto.SubscribeRoomResponse.newBuilder()
                .setStatus(GeneralStatus.CREATED.getCode())
                .build());
    }

    @GetMapping(value = "/list/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomServiceProto.ListRoomResponse> listRooms(@PathVariable("userId") String userId) {
        RoomServiceProto.ListRoomResponse response = presentationMapper.map(
                listRoomService.getSubscribedRooms(userId)
        );
        LOGGER.info("Sending ListRoomResponse {}", response);
        return ResponseEntity.ok(response);
    }
}
