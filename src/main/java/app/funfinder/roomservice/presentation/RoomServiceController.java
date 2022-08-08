package app.funfinder.roomservice.presentation;

import app.funfinder.roomservice.application.CreateRoomService;
import app.funfinder.roomservice.application.ListRoomService;
import app.funfinder.roomservice.presentation.mapper.PresentationMapper;
import app.funfinder.protobuf.RoomServiceProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static app.funfinder.roomservice.domain.kafka.KafkaTopics.*;

@Component
public class RoomServiceController {

    private final Logger LOGGER = LoggerFactory.getLogger(RoomServiceController.class);
    @Autowired
    private CreateRoomService createRoomService;

    @Autowired
    private ListRoomService listRoomService;
    @Autowired
    private PresentationMapper presentationMapper;

    @KafkaListener(topics = ROOM_SERVICE_CREATE_TOPIC, groupId = GROUP_CREATE_ROOM)
    public void createRoom(RoomServiceProto.CreateRoomRequest payload) {
        LOGGER.info("Processing message {}", payload);
        RoomServiceProto.CreateRoomResponse response = presentationMapper.map(
                createRoomService.createRoom(presentationMapper.map(payload))
        );
        LOGGER.info("Sending CreateRoomResponse {}", response);
    }

    @KafkaListener(topics = ROOM_SERVICE_LIST_TOPIC, groupId = GROUP_LIST_ROOMS)
    public void listRoom(RoomServiceProto.ListRoomRequest payload) {
        LOGGER.info("Processing message {}", payload);
        RoomServiceProto.ListRoomResponse response = presentationMapper.map(
                listRoomService.listRoom(presentationMapper.map(payload))
        );
        LOGGER.info("Sending ListRoomResponse {}", response);
    }
}
