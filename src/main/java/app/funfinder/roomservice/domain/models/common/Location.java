package app.funfinder.roomservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private Long latitude;
    private Long longitude;
}
