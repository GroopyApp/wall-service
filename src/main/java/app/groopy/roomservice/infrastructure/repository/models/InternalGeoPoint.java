package app.groopy.roomservice.infrastructure.repository.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternalGeoPoint {
    private Float latitude;
    private Float longitude;
    private Integer distanceAvailability;
}
