package app.groopy.roomservice.domain.models;

import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.domain.models.common.GeneralStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRoomInternalResponse {
    private RoomDetails room;
}
