package app.groopy.wallservice.domain.utils;

import app.groopy.wallservice.domain.models.CreateTopicRequestDto;

import java.util.UUID;

public class UUIDUtils {

    public static String generateUUID(CreateTopicRequestDto createTopicRequest) {
        var inputString = createTopicRequest.getWallId() +
                createTopicRequest.getLanguage() +
                String.join("", createTopicRequest.getCategories());
        return UUID.nameUUIDFromBytes(inputString.getBytes())
                .toString();
    }
}
