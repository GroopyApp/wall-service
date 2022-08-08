package app.funfinder.roomservice.infrastructure.elasticsearch.repository.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ESRoomSearchRequest {

    private Long latitude;
    private Long longitude;
    private Integer distanceAvailability;
    private List<String> hashtags;
    private List<String> languages;
}
