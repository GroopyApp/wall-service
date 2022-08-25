package app.groopy.roomservice.infrastructure.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;
import app.groopy.protobuf.RoomServiceProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomServiceKafkaDeserializer implements Deserializer<Object> {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceKafkaDeserializer.class);

    @SneakyThrows
    @Override
    public Object deserialize(String topic, byte[] data) {
        logger.info("trying to convert byte data into proto message given topic: {}", topic);
        switch (topic) {
            case KafkaTopics.ROOM_SERVICE_CREATE_TOPIC:
                return RoomServiceProto.CreateRoomRequest.parseFrom(data);
            case KafkaTopics.ROOM_SERVICE_LIST_TOPIC:
                return RoomServiceProto.ListRoomRequest.parseFrom(data);
        }
        return null;
    }
}