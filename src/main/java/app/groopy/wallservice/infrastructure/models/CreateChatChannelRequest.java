package app.groopy.wallservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateChatChannelRequest {
    String name;
    String group;
    String uuid;
}
