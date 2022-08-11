package app.funfinder.roomservice.application;

import app.funfinder.roomservice.domain.models.ListRoomInternalRequest;
import app.funfinder.roomservice.domain.models.ListRoomInternalResponse;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.Status;
import app.funfinder.roomservice.domain.validators.ListRoomValidator;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.ElasticsearchRoomRepository;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESPoint;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESRoomSearchRequest;
import app.funfinder.roomservice.domain.models.common.SearchScope;
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
    private ElasticsearchRoomRepository esRoomRepository;

    public ListRoomInternalResponse listRoom(ListRoomInternalRequest request, SearchScope searchScope) {
        validator.validate(request);

        List<RoomDetails> result = esRoomRepository.findBySearchRequest(ESRoomSearchRequest.builder()
                .point(ESPoint.builder()
                        .latitude(request.getActualLatitude())
                        .longitude(request.getActualLongitude())
                        .distanceAvailability(request.getSearchRangeInMeters())
                        .build())
                .hashtags(request.getHashtags())
                .languages(request.getLanguages())
                .build(), searchScope);
        return ListRoomInternalResponse.builder()
                .responseStatus(Status.COMPLETED)
                .rooms(result)
                .build();
    }
}
