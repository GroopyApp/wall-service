package app.groopy.roomservice.application;

import app.groopy.roomservice.domain.models.ListRoomRequestDto;
import app.groopy.roomservice.domain.models.ListRoomResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDTO;
import app.groopy.roomservice.application.validators.ListRoomValidator;
import app.groopy.roomservice.infrastructure.repository.models.InternalGeoPoint;
import app.groopy.roomservice.infrastructure.repository.models.RoomSearchRequest;
import app.groopy.roomservice.domain.models.common.SearchScope;
import app.groopy.roomservice.infrastructure.providers.ElasticsearchProvider;
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
    private ElasticsearchProvider elasticSearchProvider;

    public ListRoomResponseDto searchRoom(ListRoomRequestDto request, SearchScope searchScope) {
        validator.validate(request);

        List<RoomDetailsDTO> result = elasticSearchProvider.findBySearchRequest(RoomSearchRequest.builder()
                .point(InternalGeoPoint.builder()
                        .latitude(request.getActualLatitude())
                        .longitude(request.getActualLongitude())
                        .distanceAvailability(request.getSearchRangeInMeters())
                        .build())
                .hashtags(request.getHashtags())
                .languages(request.getLanguages())
                .build(), searchScope);
        return ListRoomResponseDto.builder()
                .rooms(result)
                .build();
    }

    @SneakyThrows
    public ListRoomResponseDto listRoom(String userId) {
        validator.validate(userId);

        List<RoomDetailsDTO> result = elasticSearchProvider.findByUserId(userId);
        return ListRoomResponseDto.builder()
                .rooms(result)
                .build();
    }
}
