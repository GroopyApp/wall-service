package app.groopy.roomservice.application;

import app.groopy.roomservice.domain.models.SubscribeInternalResponse;
import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.ElasticsearchRoomRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    @Autowired
    private ElasticsearchRoomRepository elasticSearchRoomRepository;

    @SneakyThrows
    public SubscribeInternalResponse subscribe(String userId, String roomId) {
        RoomDetails room = elasticSearchRoomRepository.subscribeUserToRoom(userId, roomId);
        return SubscribeInternalResponse.builder()
                .userId(userId)
                .room(room)
                .build();
    }


}
