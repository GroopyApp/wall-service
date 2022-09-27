package app.groopy.roomservice.application;

import app.groopy.roomservice.domain.models.ListRoomInternalRequest;
import app.groopy.roomservice.domain.models.ListRoomInternalResponse;
import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.application.validators.ListRoomValidator;
import app.groopy.roomservice.infrastructure.repository.models.InternalGeoPoint;
import app.groopy.roomservice.infrastructure.repository.models.RoomSearchRequest;
import app.groopy.roomservice.domain.models.common.SearchScope;
import app.groopy.roomservice.infrastructure.services.ElasticsearchRoomService;
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
    private ElasticsearchRoomService elasticSearchRoomService;

    public ListRoomInternalResponse searchRoom(ListRoomInternalRequest request, SearchScope searchScope) {
        validator.validate(request);

        List<RoomDetails> result = elasticSearchRoomService.findBySearchRequest(RoomSearchRequest.builder()
                .point(InternalGeoPoint.builder()
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

        List<RoomDetails> result = elasticSearchRoomService.findByUserId(userId);
        return ListRoomInternalResponse.builder()
                .rooms(result)
                .build();
    }
}
