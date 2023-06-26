package app.groopy.wallservice.domain.utils;

import app.groopy.wallservice.domain.models.requests.CreateEventRequestDto;
import app.groopy.wallservice.domain.models.requests.CreateTopicRequestDto;

import java.util.UUID;

public class Utils {

    public static String generateChatName(String topicName) {
        return "chat--" + topicName.toLowerCase().replaceAll(" ", "-");
    }

    public static String generateChatGroupName(String topicName) {
        return "group--" + topicName.toLowerCase().replaceAll(" ", "-");
    }

    public static String generateUUID(CreateTopicRequestDto createTopicRequest) {
        var inputString = createTopicRequest.getWallId() +
                createTopicRequest.getLanguage() +
                String.join("", createTopicRequest.getCategories());
        return UUID.nameUUIDFromBytes(inputString.getBytes())
                .toString();
    }

    public static String generateUUID(CreateEventRequestDto createTopicRequest) {
        var inputString = createTopicRequest.getTopicId() +
                createTopicRequest.getLocationId() +
                createTopicRequest.getStartDate().toString() +
                createTopicRequest.getEndDate().toString();
        return UUID.nameUUIDFromBytes(inputString.getBytes())
                .toString();
    }
}
