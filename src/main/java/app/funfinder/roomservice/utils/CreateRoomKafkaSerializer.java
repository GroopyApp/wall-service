package app.funfinder.roomservice.utils;

import app.funfinder.protobuf.RoomServiceProto;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class CreateRoomKafkaSerializer implements Serializer<RoomServiceProto.CreateRoomRequest> {

    private static final Logger logger = LoggerFactory.getLogger(CreateRoomKafkaSerializer.class);

    @SneakyThrows
    @Override
    public byte[] serialize(String topic, RoomServiceProto.CreateRoomRequest data) {
        return data.toByteArray();
    }
}