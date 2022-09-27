package app.groopy.roomservice.infrastructure.repository.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomSearchRequest {

    private InternalGeoPoint point;
    private List<String> hashtags;
    private List<String> languages;
}
