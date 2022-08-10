package app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ESPoint {
    private Float latitude;
    private Float longitude;
    private Integer distanceAvailability;
}
