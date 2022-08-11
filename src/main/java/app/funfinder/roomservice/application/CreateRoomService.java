package app.funfinder.roomservice.application;

import app.funfinder.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import app.funfinder.roomservice.domain.models.CreateRoomInternalResponse;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.Status;
import app.funfinder.roomservice.domain.validators.CreateRoomValidator;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.ElasticsearchRoomRepository;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.entities.ESRoomEntity;
import app.funfinder.roomservice.utils.CoordsUtils;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateRoomService {

    private final Logger logger = LoggerFactory.getLogger(CreateRoomService.class);

    @Autowired
    private CreateRoomValidator validator;

    @Autowired
    private ElasticsearchRoomRepository elasticSearchRoomRepository;

   @SneakyThrows
    public CreateRoomInternalResponse createRoom(CreateRoomInternalRequest request) {
        validator.validate(request);

        final String roomId = UUID.nameUUIDFromBytes(assembleId(request)).toString();

        NewTopic topic = TopicBuilder.name(roomId)
                .partitions(6)
                .replicas(3)
                .compact()
                .build();

        elasticSearchRoomRepository.save(
                ESRoomEntity.builder()
                        .roomId(roomId)
                        .roomName(request.getRoomName())
                        .hashtags(request.getHashtags())
                        .languages(request.getLanguages())
                        .location(CoordsUtils.getCoordString(
                                request.getRoomLocation().getLatitude(),
                                request.getRoomLocation().getLongitude()))
                        .build());

        logger.info("topic for chat room {} correctly created: {}", request.getRoomName(), topic);

        return CreateRoomInternalResponse.builder()
                .responseStatus(Status.CREATED)
                .room(RoomDetails.builder()
                        .roomId(topic.name())
                        .roomName(request.getRoomName())
                        .build())
                .build();
    }

    private byte[] assembleId(CreateRoomInternalRequest request) {
        StringBuilder sb = new StringBuilder(request.getRoomName());
        request.getHashtags().forEach(sb::append);
        request.getLanguages().forEach(sb::append);
        sb.append(
                request.getRoomLocation().getLatitude())
                .append(
                 request.getRoomLocation().getLongitude()
                );
        return sb.toString().getBytes();
    }
}
