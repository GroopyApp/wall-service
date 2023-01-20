package app.groopy.roomservice.application;

import app.groopy.roomservice.domain.models.SubscribeResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDTO;
import app.groopy.roomservice.infrastructure.providers.ElasticsearchProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    @Autowired
    private ElasticsearchProvider elasticSearchProvider;

    @SneakyThrows
    public SubscribeResponseDto subscribe(String userId, String roomId) {
        RoomDetailsDTO room = elasticSearchProvider.subscribeUserToRoom(userId, roomId);
        return SubscribeResponseDto.builder()
                .userId(userId)
                .room(room)
                .build();
    }


}
