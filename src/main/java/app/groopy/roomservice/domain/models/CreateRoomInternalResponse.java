package app.groopy.roomservice.domain.models;

import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.domain.models.common.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRoomInternalResponse {

    private Status responseStatus;
    private RoomDetails room;
}
