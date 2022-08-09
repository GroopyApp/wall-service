package app.funfinder.roomservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomLocation {
    private Float latitude;
    private Float longitude;
    private Integer rangeInMeters;
}
