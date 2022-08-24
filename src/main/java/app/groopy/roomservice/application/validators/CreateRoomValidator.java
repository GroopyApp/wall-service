package app.groopy.roomservice.application.validators;

import app.groopy.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.groopy.roomservice.domain.exceptions.RoomWithExistingNameException;
import app.groopy.roomservice.domain.exceptions.SimilarRoomsExistException;
import app.groopy.roomservice.domain.models.CreateRoomInternalRequest;
import app.groopy.roomservice.domain.models.common.RoomDetails;
import app.groopy.roomservice.domain.models.common.SearchScope;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.ElasticsearchRoomRepository;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESPoint;
import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESRoomSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateRoomValidator {

    @Autowired
    private ElasticsearchRoomRepository elasticSearchRoomRepository;
    private static final String HASHTAG_REGEX = "^#[^ !@#$%^&*(),.?\":{}|<>]*$";
    private static final String LANGUAGE_REGEX = "^[a-z]{2}-[A-Z]{2}$";

    public void validate(CreateRoomInternalRequest request) throws CreateRoomValuesValidationException, SimilarRoomsExistException, RoomWithExistingNameException {
        patternMatch(HASHTAG_REGEX, request.getHashtags(), "hashtags");
        patternMatch(LANGUAGE_REGEX, request.getLanguages(), "languages");
        roomDoesntExists(request);
        isUserAllowed(request.getCreator());
    }

    private void roomDoesntExists(CreateRoomInternalRequest request) throws RoomWithExistingNameException, SimilarRoomsExistException {
        RoomDetails roomWithSameName = elasticSearchRoomRepository.findByRoomName(request.getRoomName());
        if (roomWithSameName != null) {
            throw new RoomWithExistingNameException(request.getRoomName());
        }

        List<RoomDetails> similarRooms = elasticSearchRoomRepository.findBySearchRequest(ESRoomSearchRequest.builder()
                        .point(ESPoint.builder()
                                .latitude(request.getRoomLocation().getLatitude())
                                .longitude(request.getRoomLocation().getLongitude())
                                .build())
                        .hashtags(request.getHashtags())
                        .languages(request.getLanguages())
                .build(), SearchScope.LOOKING_FOR_SIMILAR);

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
