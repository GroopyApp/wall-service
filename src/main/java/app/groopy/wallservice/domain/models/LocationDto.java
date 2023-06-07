package app.groopy.wallservice.domain.models;

import lombok.Value;

@Value
public class LocationDto {
    String locationId;
    float latitude;
    float longitude;
}
