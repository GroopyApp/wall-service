package app.groopy.roomservice.application;

import app.groopy.roomservice.domain.models.ListRoomInternalRequest;
import app.groopy.roomservice.domain.models.ListRoomInternalResponse;
import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.domain.models.common.GeneralStatus;
import app.groopy.roomservice.application.validators.ListRoomValidator;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.ElasticsearchRoomRepository;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESPoint;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESRoomSearchRequest;
import app.groopy.roomservice.domain.models.common.SearchScope;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListRoomService {

    private final Logger LOGGER = LoggerFactory.getLogger(ListRoomService.class);

    @Autowired
    private ListRoomValidator validator;

    @Autowired
    private ElasticsearchRoomRepository elasticSearchRoomRepository;

    public ListRoomInternalResponse searchRoom(ListRoomInternalRequest request, SearchScope searchScope) {
        validator.validate(request);

        List<RoomDetails> result = elasticSearchRoomRepository.findBySearchRequest(ESRoomSearchRequest.builder()
                .point(ESPoint.builder()
                        .latitude(request.getActualLatitude())
                        .longitude(request.getActualLongitude())
                        .distanceAvailability(request.getSearchRangeInMeters())
                        .build())
                .hashtags(request.getHashtags())
                .languages(request.getLanguages())
                .build(), searchScope);
        return ListRoomInternalResponse.builder()
                .rooms(result)
                .build();
    }

    @SneakyThrows
    public ListRoomInternalResponse listRoom(String userId) {
        validator.validate(userId);

        List<RoomDetails> result = elasticSearchRoomRepository.findByUserId(userId);
        return ListRoomInternalResponse.builder()
                .rooms(result)
                .build();
    }
}
