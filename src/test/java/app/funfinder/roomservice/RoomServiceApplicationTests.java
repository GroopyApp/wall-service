package app.funfinder.roomservice;

import app.funfinder.roomservice.presentation.RoomServiceProducer;
import app.funfinder.roomservice.domain.kafka.KafkaTopics;
import app.funfinder.protobuf.RoomServiceProto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RoomServiceApplicationTests {

    //TODO this is a sample test to send proto messages and check they works, use debug to verify that then remove this test

    @Autowired
    private RoomServiceProducer producer;

    @Test
    public void consumeCreateRoomMessage() throws Exception
    {
        final RoomServiceProto.CreateRoomRequest userMessage = RoomServiceProto.CreateRoomRequest.newBuilder()
                .setRoomName("test-room")
                .addAllHashtags(List.of("#test1", "#test2"))
                .addAllLanguages(List.of("it-IT"))
                .setUserId("aled94")
                .setLatitude(0L)
                .setLongitude(0L)
                .setRangeInMeters(3000)
                .build();

        //this will never happen in a real environment, room-service will only send to kafka broker room creation response, this should come from another service
        producer.send(KafkaTopics.ROOM_SERVICE_CREATE_TOPIC, userMessage);
        Thread.sleep(10000);

        final RoomServiceProto.ListRoomRequest listRoomRequest = RoomServiceProto.ListRoomRequest.newBuilder()
                .addAllHashtags(List.of("#test1", "#test2"))
                .addAllLanguages(List.of("it-IT"))
                .setLongitude(0L)
                .setLatitude(0L)
                .setSearchRangeInMeters(5000)
                .setUserId("aaa")
                .build();

        producer.send(KafkaTopics.ROOM_SERVICE_LIST_TOPIC, listRoomRequest);

        Thread.sleep(10000);
    }

}
