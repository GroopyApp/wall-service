package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SubscribeEventRequestDto {
    String userId;
    String eventId;
}
