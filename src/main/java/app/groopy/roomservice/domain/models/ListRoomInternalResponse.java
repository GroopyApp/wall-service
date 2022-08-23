package app.groopy.roomservice.domain.models;

import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.domain.models.common.GeneralStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListRoomInternalResponse {

    private GeneralStatus responseStatus;
    private List<RoomDetails> rooms;
}
