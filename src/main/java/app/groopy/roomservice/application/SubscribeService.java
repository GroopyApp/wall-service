package app.groopy.roomservice.application;

import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.domain.models.SubscribeResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import app.groopy.roomservice.infrastructure.ElasticsearchInfrastructureService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    @Autowired
    private ApplicationMapper mapper;
    @Autowired
    private ElasticsearchInfrastructureService infrastructureService;

    @SneakyThrows
    public SubscribeResponseDto subscribe(String userId, String roomId) {
        RoomDetailsDto room = mapper.map(infrastructureService.subscribeUserToRoom(userId, roomId));
        return SubscribeResponseDto.builder()
                .userId(userId)
                .room(room)
                .build();
    }


}
