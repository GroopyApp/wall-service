package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SubscribeTopicRequestDto {
    String userId;
    String topicId;
}
