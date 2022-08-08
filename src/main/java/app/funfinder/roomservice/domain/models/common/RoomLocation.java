package app.funfinder.roomservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomLocation {
    private Long latitude;
    private Long longitude;
    private Integer rangeInMeters;
}
