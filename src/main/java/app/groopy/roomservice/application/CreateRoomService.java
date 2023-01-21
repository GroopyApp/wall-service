package app.groopy.roomservice.application;

import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.domain.models.CreateRoomRequestDto;
import app.groopy.roomservice.domain.models.CreateRoomResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import app.groopy.roomservice.application.validators.CreateRoomValidator;
import app.groopy.roomservice.infrastructure.ElasticsearchInfrastructureService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateRoomService extends app.groopy.commons.application.Service<CreateRoomRequestDto, CreateRoomResponseDto> {

    private final Logger logger = LoggerFactory.getLogger(CreateRoomService.class);

    @Autowired
    private CreateRoomValidator validator;
    @Autowired
    private ApplicationMapper mapper;
    @Autowired
    private ElasticsearchInfrastructureService infrastructureService;

   @SneakyThrows
    public CreateRoomResponseDto perform(CreateRoomRequestDto request) {
        validator.validate(request);

        final String roomId = UUID.nameUUIDFromBytes(assembleId(request)).toString();

//        NewTopic topic = TopicBuilder.name(roomId)
//                .partitions(6)
//                .replicas(3)
//                .compact()
//                .build();

        RoomDetailsDto result = mapper.map(infrastructureService.storeRoom(roomId, request));

       infrastructureService.subscribeUserToRoom(request.getCreator(), result.getRoomId());

       logger.info("Room correctly stored in ES");
//        logger.info("topic for chat room {} correctly created: {}", request.getRoomName(), topic);

        return CreateRoomResponseDto.builder()
                .room(result)
                .build();
    }

    private byte[] assembleId(CreateRoomRequestDto request) {
        StringBuilder sb = new StringBuilder(request.getRoomName());
        request.getHashtags().forEach(sb::append);
        request.getLanguages().forEach(sb::append);
        sb.append(
                request.getRoomLocation().getLatitude())
                .append(
                 request.getRoomLocation().getLongitude()
                );
        sb.append(LocalDateTime.now());
        return sb.toString().getBytes();
    }
}
