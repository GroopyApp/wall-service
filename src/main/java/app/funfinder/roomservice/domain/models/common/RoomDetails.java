package app.funfinder.roomservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomDetails {
    private String roomId;
    private String roomName;
    private Long latitude;
    private Long longitude;
    private Integer distance;
}
