package app.groopy.roomservice.application;

import app.groopy.roomservice.domain.models.CreateRoomRequestDto;
import app.groopy.roomservice.domain.models.CreateRoomResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDTO;
import app.groopy.roomservice.domain.models.common.RoomStatus;
import app.groopy.roomservice.application.validators.CreateRoomValidator;
import app.groopy.roomservice.infrastructure.repository.models.entities.ESRoomEntity;
import app.groopy.roomservice.infrastructure.providers.ElasticsearchProvider;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateRoomService {

    private final Logger logger = LoggerFactory.getLogger(CreateRoomService.class);

    @Autowired
    private CreateRoomValidator validator;

    @Autowired
    private ElasticsearchProvider elasticSearchProvider;

   @SneakyThrows
    public CreateRoomResponseDto createRoom(CreateRoomRequestDto request) {
        validator.validate(request);

        final String roomId = UUID.nameUUIDFromBytes(assembleId(request)).toString();

//        NewTopic topic = TopicBuilder.name(roomId)
//                .partitions(6)
//                .replicas(3)
//                .compact()
//                .build();

        RoomDetailsDTO result = elasticSearchProvider.save(
                ESRoomEntity.builder()
                        .roomId(roomId)
                        .roomName(request.getRoomName())
                        .hashtags(request.getHashtags())
                        .languages(request.getLanguages())
//                        .status(RoomStatus.PENDING) TODO implement this and remove line below
                        .status(RoomStatus.CREATED)
                        .location(new GeoPoint(
                                request.getRoomLocation().getLatitude(),
                                request.getRoomLocation().getLongitude()))
                        .build());

       elasticSearchProvider.subscribeUserToRoom(request.getCreator(), result.getRoomId());

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
