package app.funfinder.roomservice.infrastructure.kafka;

public class KafkaTopics {

    private final static String ROOM_SERVICE_TOPIC = "room-service";
    public static final String ROOM_SERVICE_CREATE_TOPIC = ROOM_SERVICE_TOPIC + "_create";
    public static final String ROOM_SERVICE_LIST_TOPIC = ROOM_SERVICE_TOPIC + "_list";

    public static final String GROUP_CREATE_ROOM = "create";

    public static final String GROUP_LIST_ROOMS = "list";

}
