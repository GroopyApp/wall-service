package app.funfinder.roomservice.domain.validators;

import app.funfinder.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.domain.models.common.RoomLocation;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.ElasticsearchRoomRepository;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESPoint;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.dtos.ESRoomSearchRequest;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.entities.ESRoomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static app.funfinder.roomservice.utils.CoordsUtils.DEFAULT_SEARCH_RANGE_IN_METERS;

@Component
public class CreateRoomValidator {

    private static final String HASHTAG_REGEX = "^#[^ !@#$%^&*(),.?\":{}|<>]*$";
    private static final String LANGUAGE_REGEX = "^[a-z]{2}-[A-Z]{2}$";

    public void validate(CreateRoomInternalRequest request) throws CreateRoomValuesValidationException {

        patternMatch(HASHTAG_REGEX, request.getHashtags(), "hashtags");
        patternMatch(LANGUAGE_REGEX, request.getLanguages(), "languages");
        isUserAllowed(request.getCreator());
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
