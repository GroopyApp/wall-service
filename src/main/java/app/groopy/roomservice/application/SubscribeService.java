package app.groopy.roomservice.application;

import app.groopy.roomservice.infrastructure.elasticsearch.repository.ElasticsearchRoomRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    @Autowired
    private ElasticsearchRoomRepository elasticSearchRoomRepository;

    @SneakyThrows
    public void subscribe(String userId, String roomId) {
        elasticSearchRoomRepository.subscribeUserToRoom(userId, roomId);
    }


}