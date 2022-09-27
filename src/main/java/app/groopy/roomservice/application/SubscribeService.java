package app.groopy.roomservice.application;

import app.groopy.roomservice.domain.models.SubscribeInternalResponse;
import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.infrastructure.services.ElasticsearchRoomService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    @Autowired
    private ElasticsearchRoomService elasticSearchRoomService;

    @SneakyThrows
    public SubscribeInternalResponse subscribe(String userId, String roomId) {
        RoomDetails room = elasticSearchRoomService.subscribeUserToRoom(userId, roomId);
        return SubscribeInternalResponse.builder()
                .userId(userId)
                .room(room)
                .build();
    }


}
