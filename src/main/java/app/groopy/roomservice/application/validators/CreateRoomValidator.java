package app.groopy.roomservice.application.validators;

import app.groopy.commons.infrastructure.providers.ElasticsearchProvider;
import app.groopy.commons.infrastructure.repository.models.InternalGeoPoint;
import app.groopy.commons.infrastructure.repository.models.RoomSearchRequest;
import app.groopy.commons.infrastructure.repository.models.SearchScope;
import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.groopy.roomservice.domain.exceptions.RoomWithExistingNameException;
import app.groopy.roomservice.domain.exceptions.SimilarRoomsExistException;
import app.groopy.roomservice.domain.models.CreateRoomRequestDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateRoomValidator {

    @Autowired
    private ApplicationMapper mapper;
    @Autowired
    private ElasticsearchProvider elasticSearchProvider;

    private static final String HASHTAG_REGEX = "^#[^ !@#$%^&*(),.?\":{}|<>]*$";
    private static final String LANGUAGE_REGEX = "^[a-z]{2}-[A-Z]{2}$";

    public void validate(CreateRoomRequestDto request) throws CreateRoomValuesValidationException, SimilarRoomsExistException, RoomWithExistingNameException {
        patternMatch(HASHTAG_REGEX, request.getHashtags(), "hashtags");
        patternMatch(LANGUAGE_REGEX, request.getLanguages(), "languages");
        roomDoesntExists(request);
        isUserAllowed(request.getCreator());
    }

    private void roomDoesntExists(CreateRoomRequestDto request) throws RoomWithExistingNameException, SimilarRoomsExistException {
        RoomDetailsDto roomWithSameName = mapper.map(elasticSearchProvider.findByRoomName(request.getRoomName()));
        if (roomWithSameName != null) {
            throw new RoomWithExistingNameException(request.getRoomName());
        }

        List<RoomDetailsDto> similarRooms = elasticSearchProvider.findBySearchRequest(RoomSearchRequest.builder()
                        .point(InternalGeoPoint.builder()
                                .latitude(request.getRoomLocation().getLatitude())
                                .longitude(request.getRoomLocation().getLongitude())
                                .build())
                        .hashtags(request.getHashtags())
                        .languages(request.getLanguages())
                .build(), SearchScope.LOOKING_FOR_SIMILAR).stream().map(entity -> mapper.map(entity)).collect(Collectors.toList());

        if (!similarRooms.isEmpty()) {
            throw new SimilarRoomsExistException(request.getHashtags(), request.getLanguages(), similarRooms);
        }
    }

    private void patternMatch(String pattern, List<String> values, String fieldName) throws CreateRoomValuesValidationException {
        for (String value : values) {
            if (!value.matches(pattern)) {
                throw new CreateRoomValuesValidationException(value, fieldName);
            }
        }
    }

    private void isUserAllowed(String creator) {
        //TODO implement this
    }
}
