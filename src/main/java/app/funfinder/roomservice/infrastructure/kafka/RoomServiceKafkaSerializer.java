package app.funfinder.roomservice.infrastructure.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class RoomServiceKafkaSerializer implements Serializer<Object> {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceKafkaSerializer.class);

    @SneakyThrows
    @Override
    public byte[] serialize(String topic, Object data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(data);
        oos.flush();
        return bos.toByteArray();
    }
}