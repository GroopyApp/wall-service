package app.groopy.roomservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomLocationDto {
    private Float latitude;
    private Float longitude;
}
