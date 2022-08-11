package app.funfinder.roomservice.domain.models;

import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.Status;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListRoomInternalResponse {

    private Status responseStatus;
    private List<RoomDetails> rooms;
}
