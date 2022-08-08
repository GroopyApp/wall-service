package app.funfinder.roomservice.presentation;

import app.funfinder.protobuf.RoomServiceProto;;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceProducer {

    private final Logger logger = LoggerFactory.getLogger(RoomServiceProducer.class);

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public void send(final String topic, final RoomServiceProto.CreateRoomRequest message) {
        logger.info("Producing message [{}]", message);
        kafkaTemplate.send(topic, message.toByteArray());
    }

    public void send(final String topic, final RoomServiceProto.ListRoomRequest message) {
        logger.info("Producing message [{}]", message);
        kafkaTemplate.send(topic, message.toByteArray());
    }
}
