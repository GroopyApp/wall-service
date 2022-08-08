package app.funfinder.roomservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListRoomInternalRequest {
    private Long actualLatitude;
    private Long actualLongitude;
    private Integer searchRangeInMeters;
    private List<String> hashtags;
    private List<String> languages;
    private String userId;
}
