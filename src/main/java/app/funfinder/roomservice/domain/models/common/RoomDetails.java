package app.funfinder.roomservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomDetails {
    private String roomId;
    private String roomName;
    private Float latitude;
    private Float longitude;
    private Integer distance;
}
