package app.funfinder.roomservice;

import app.funfinder.roomservice.presentation.RoomServiceConsumer;
import app.funfinder.roomservice.presentation.RoomServiceProducer;
import app.funfinder.roomservice.utils.kafka.KafkaTopics;
import app.funfinder.protobuf.RoomServiceProto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class RoomServiceApplicationTests {


    @Autowired
    private RoomServiceProducer producer;
    @Autowired
    private RoomServiceConsumer consumer;


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
                .build();

        //this will never happen in a real environment, room-service will only send to kafka broker room creation response, this should come from another service
        producer.send(KafkaTopics.CREATE_ROOM_TOPIC, userMessage);
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(consumer.getLatch().getCount() == 0L);
    }

}
