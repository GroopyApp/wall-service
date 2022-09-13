package app.groopy.roomservice.domain.models;

import app.groopy.roomservice.domain.models.common.RoomDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribeInternalResponse {
    private String userId;
    private RoomDetails room;
}
