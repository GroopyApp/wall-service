package app.funfinder.roomservice;

import app.funfinder.protobuf.RoomServiceProto;
import app.funfinder.roomservice.domain.kafka.RoomServiceKafkaDeserializer;
import app.funfinder.roomservice.utils.CreateRoomKafkaSerializer;
import app.funfinder.roomservice.utils.ListRoomKafkaSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.*;

public class KafkaClientTester {

    private static String topicName;
    private static KafkaTemplate<String, Object> kafkaTemplate;

    public static void main(String[] args) {
        while (true) {
            runMainTask();
        }
    }

    private static void runMainTask() {
        System.out.println("Waiting for next command... (list / create / exit)\n");
        final Scanner sc = new Scanner(System.in);

        String command = sc.nextLine();

        if (!command.isBlank() && !command.isEmpty()) {
            kafkaTemplate = kafkaTemplate(command);

            switch (command) {
                case "create1":
                    runCreateRoomTask();
                    break;
                case "create2":
                    runCreateRoom2Task();
                    break;
                case "list1":
                    runListRoomTask();
                    break;
                case "list2":
                    runList2RoomTask();
                    break;
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Command not found...");
                    runMainTask();
                    break;
            }
        }
        runMainTask();
    }

    private static void runList2RoomTask() {
        topicName = "room-service_list";

        send(RoomServiceProto.ListRoomRequest.newBuilder()
                .addAllHashtags(List.of("#test1", "#test2"))
                .build()
        );

    }

    private static void runListRoomTask() {
        topicName = "room-service_list";
        send(RoomServiceProto.ListRoomRequest.newBuilder()
                .addAllHashtags(List.of("#test1", "#test2"))
                .addAllLanguages(List.of("it-IT"))
//                .setLatitude(1)
//                .setLongitude(2)
//                .setSearchRangeInMeters(3000)
                .build()
        );
    }

    private static void runCreateRoomTask() {

        topicName = "room-service_create";

        send(RoomServiceProto.CreateRoomRequest.newBuilder()
                .setRoomName(UUID.randomUUID().toString())
                .addAllHashtags(List.of("#test1", "#test2"))
                .addAllLanguages(List.of("it-IT"))
                .setUserId("aled94")
                .setLatitude(1)
                .setLongitude(2)
                .setRangeInMeters(3000)
                .build()
        );
    }

    private static void runCreateRoom2Task() {

        topicName = "room-service_create";

        send(RoomServiceProto.CreateRoomRequest.newBuilder()
                .setRoomName(UUID.randomUUID().toString())
                .addAllHashtags(List.of("#test3", "#test4"))
                .addAllLanguages(List.of("it-IT"))
                .setUserId("aled94")
                .setLatitude(1)
                .setLongitude(2)
                .setRangeInMeters(10000).build()
        );
    }

    private static void send(Object message) {
        kafkaTemplate.send(topicName, message);
    }

    private static ProducerFactory<String, Object> producerFactory(String command) {
        Class serializer = null;
        switch (command) {
            case "list1":
            case "list2":
                serializer = ListRoomKafkaSerializer.class;
                break;
            case "create1":
            case "create2":
                serializer = CreateRoomKafkaSerializer.class;
                break;
        }
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                serializer);
        configProps.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                RoomServiceKafkaDeserializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private static KafkaTemplate<String, Object> kafkaTemplate(String command) {
        return new KafkaTemplate<>(producerFactory(command));
    }
}
