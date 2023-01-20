package app.groopy.roomservice.domain.models;

import app.groopy.roomservice.domain.models.common.RoomLocation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRoomRequestDto {

    private String roomName;
    private List<String> hashtags;
    private List<String> languages;
    private RoomLocation roomLocation;
    private String creator;
}
