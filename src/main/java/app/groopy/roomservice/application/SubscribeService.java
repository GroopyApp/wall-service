package app.groopy.roomservice.application;

import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.domain.exceptions.SubscribeRoomException;
import app.groopy.roomservice.domain.models.SubscribeResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import app.groopy.roomservice.infrastructure.ElasticsearchInfrastructureService;
import app.groopy.roomservice.infrastructure.exceptions.ElasticsearchServiceException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    protected final Logger LOGGER = LoggerFactory.getLogger(SubscribeService.class);

    @Autowired
    private ApplicationMapper mapper;
    @Autowired
    private ElasticsearchInfrastructureService infrastructureService;

    @SneakyThrows
    public SubscribeResponseDto subscribe(String userId, String roomId) {
        try {
            RoomDetailsDto room = mapper.map(infrastructureService.subscribeUserToRoom(userId, roomId));
            return SubscribeResponseDto.builder()
                    .userId(userId)
                    .room(room)
                    .build();
        } catch (ElasticsearchServiceException e) {
            LOGGER.error("an error occurred trying to search for rooms for a user", e);
            throw new SubscribeRoomException(userId, roomId, e.getLocalizedMessage());
        }
    }


}
