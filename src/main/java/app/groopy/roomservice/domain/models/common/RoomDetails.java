package app.groopy.roomservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomDetails {
    private String roomId;
    private String roomName;
    private Float latitude;
    private Float longitude;
    private RoomStatus status;
    private List<String> hashtags;
    private List<String> languages;
}
