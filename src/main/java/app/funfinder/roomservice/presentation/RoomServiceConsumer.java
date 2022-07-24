package app.funfinder.roomservice.presentation;

import app.funfinder.roomservice.application.CreateRoomService;
import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import app.funfinder.roomservice.domain.models.CreateRoomInternalResponse;
import app.funfinder.roomservice.domain.models.common.Location;
import app.funfinder.protobuf.RoomServiceProto;;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

import static app.funfinder.roomservice.utils.kafka.KafkaTopics.CREATE_ROOM_TOPIC;

@Service
public class RoomServiceConsumer {

    private final Logger logger = LoggerFactory.getLogger(RoomServiceConsumer.class);
    private CountDownLatch latch = new CountDownLatch(1);
    @Autowired
    private CreateRoomService createRoomService;


    @KafkaListener(topics = CREATE_ROOM_TOPIC, groupId = "room-service")
    public void createRoom(RoomServiceProto.CreateRoomRequest payload) {
        latch.countDown();
        CreateRoomInternalResponse response = createRoomService.createRoom(
                CreateRoomInternalRequest.builder()
                        .roomName(payload.getRoomName())
                        .hashtags(payload.getHashtagsList())
                        .languages(payload.getLanguagesList())
                        .location(Location.builder()
                                .latitude(payload.getLatitude())
                                .longitude(payload.getLongitude())
                                .build())
                        .creator(payload.getUserId())
                        .build());
    }

    public CountDownLatch getLatch() {
        return this.latch;
    }
}
