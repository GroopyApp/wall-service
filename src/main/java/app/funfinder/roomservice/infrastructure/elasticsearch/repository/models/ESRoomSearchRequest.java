package app.funfinder.roomservice.infrastructure.elasticsearch.repository.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ESRoomSearchRequest {

    private Float latitude;
    private Float longitude;
    private Integer distanceAvailability;
    private List<String> hashtags;
    private List<String> languages;
}
