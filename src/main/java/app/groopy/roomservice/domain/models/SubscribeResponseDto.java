package app.groopy.roomservice.domain.models;

import app.groopy.roomservice.domain.models.common.RoomDetailsDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscribeResponseDto {
    private String userId;
    private RoomDetailsDTO room;
}
