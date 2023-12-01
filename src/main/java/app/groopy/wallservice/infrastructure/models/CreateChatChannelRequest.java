package app.groopy.wallservice.infrastructure.models;

import app.groopy.wallservice.domain.models.GroupType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateChatChannelRequest {
    String name;
    GroupType group;
    String uuid;
}
