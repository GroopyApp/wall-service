package app.funfinder.roomservice.domain.validators;

import app.funfinder.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateRoomValidator {


    private static final String HASHTAG_REGEX = "^#[^ !@#$%^&*(),.?\":{}|<>]*$";
    private static final String LANGUAGE_REGEX = "^[a-z]{2}-[A-Z]{2}$";

    public void validate(CreateRoomInternalRequest request) throws CreateRoomValuesValidationException {

        patternMatch(HASHTAG_REGEX, request.getHashtags(), "hashtags");
        patternMatch(LANGUAGE_REGEX, request.getLanguages(), "languages");

    }

    private void patternMatch(String pattern, List<String> values, String fieldName) throws CreateRoomValuesValidationException {
        for (String value : values) {
            if (!value.matches(pattern)) {
                throw new CreateRoomValuesValidationException(value, fieldName);
            }
        }
    }
}
