package app.groopy.roomservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListRoomInternalRequest {
    private Float actualLatitude;
    private Float actualLongitude;
    private Integer searchRangeInMeters;
    private List<String> hashtags;
    private List<String> languages;
    private String userId;
}