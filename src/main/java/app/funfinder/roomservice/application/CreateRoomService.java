package app.funfinder.roomservice.application;

import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import app.funfinder.roomservice.domain.models.CreateRoomInternalResponse;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.Status;
import app.funfinder.roomservice.domain.validators.CreateRoomValidator;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomInformation;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.ESRoomRepository;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

//FIXME this should be managed by a domain bean!!!
@Service
public class CreateRoomService {

    private final Logger logger = LoggerFactory.getLogger(CreateRoomService.class);

    @Autowired
    private CreateRoomValidator validator;

    @Autowired
    private ESRoomRepository elasticSearchRoomRepository;

    public CreateRoomInternalResponse createRoom(CreateRoomInternalRequest request) {
        try {
            validator.validate(request);

            NewTopic topic = TopicBuilder.name(request.getRoomName()) //TODO this should be a unique id using UUID etc.
                    .partitions(6)
                    .replicas(3)
                    .compact()
                    .build();

            elasticSearchRoomRepository.save(
                    ESRoomInformation.builder()
                            .roomId(topic.name())
                            .roomName(request.getRoomName())
                            .hashtags(request.getHashtags())
                            .languages(request.getLanguages())
                            .point(new Point(
                                    request.getRoomLocation().getLatitude(),
                                    request.getRoomLocation().getLongitude()))
                            .build());

            logger.info("topic for chat room {} correctly created: {}", request.getRoomName(), topic);

            return CreateRoomInternalResponse.builder()
                    .responseStatus(Status.CREATED)
                    .room(RoomDetails.builder()
                            .roomId(topic.name()) //TODO this should be a unique id using UUID etc.
                            .roomName(topic.name())
                            .build())
                    .build();
        } catch (Exception e) {
            logger.info("an error occurred trying to create room: {}", request, e);
            return CreateRoomInternalResponse.builder()
                    .responseStatus(Status.UNKNOWN_ERROR)
                    .build();
        }
    }
}
