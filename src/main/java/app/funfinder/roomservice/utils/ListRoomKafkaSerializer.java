package app.funfinder.roomservice.utils;

import app.funfinder.protobuf.RoomServiceProto;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListRoomKafkaSerializer implements Serializer<RoomServiceProto.ListRoomRequest> {

    private static final Logger logger = LoggerFactory.getLogger(ListRoomKafkaSerializer.class);

    @SneakyThrows
    @Override
    public byte[] serialize(String topic, RoomServiceProto.ListRoomRequest data) {
        return data.toByteArray();
    }
}