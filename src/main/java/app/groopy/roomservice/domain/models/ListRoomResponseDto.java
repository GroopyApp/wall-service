package app.groopy.roomservice.domain.models;

import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListRoomResponseDto {
    private List<RoomDetailsDto> rooms;
}
