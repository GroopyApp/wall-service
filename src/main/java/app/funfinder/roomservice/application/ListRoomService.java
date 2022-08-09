package app.funfinder.roomservice.application;

import app.funfinder.roomservice.application.mapper.ApplicationMapper;
import app.funfinder.roomservice.domain.models.ListRoomInternalRequest;
import app.funfinder.roomservice.domain.models.ListRoomInternalResponse;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.Status;
import app.funfinder.roomservice.domain.validators.ListRoomValidator;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomInformation;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomSearchRequest;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.ESRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//FIXME this should be managed by a domain bean!!!
@Service
public class ListRoomService {

    private final Logger logger = LoggerFactory.getLogger(ListRoomService.class);

    @Autowired
    private ListRoomValidator validator;

    @Autowired
    private ESRoomRepository esRoomRepository;

    @Autowired
    private ApplicationMapper mapper;

    public ListRoomInternalResponse listRoom(ListRoomInternalRequest request) {
        try {
            validator.validate(request);
            List<ESRoomInformation> result = esRoomRepository.findBy(ESRoomSearchRequest.builder()
                            .latitude(request.getActualLatitude())
                            .longitude(request.getActualLongitude())
                            .hashtags(request.getHashtags())
                            .languages(request.getLanguages())
                            .distanceAvailability(request.getSearchRangeInMeters())
                    .build());
            return ListRoomInternalResponse.builder()
                    .rooms(result.stream()
                            .map(esInfo -> mapper.map(esInfo))
                            .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            logger.info("an error occurred trying to create room: {}", request, e);
            return ListRoomInternalResponse.builder()
                    .responseStatus(Status.UNKNOWN_ERROR)
                    .build();
        }
    }
}
