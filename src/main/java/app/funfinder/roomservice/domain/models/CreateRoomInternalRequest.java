package app.funfinder.roomservice.domain.models;

import app.funfinder.roomservice.domain.models.common.Location;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRoomInternalRequest {

    private String roomName;
    private List<String> hashtags;
    private List<String> languages;
    private Location location;
    private String creator;
}
