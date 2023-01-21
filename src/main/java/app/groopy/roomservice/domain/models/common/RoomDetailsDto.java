package app.groopy.roomservice.domain.models.common;

import app.groopy.commons.domain.models.RoomStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomDetailsDto {
    private String roomId;
    private String roomName;
    private Float latitude;
    private Float longitude;
    private RoomStatus status;
    private List<String> hashtags;
    private List<String> languages;
}
