package app.groopy.roomservice.domain.validators;

import app.groopy.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.groopy.roomservice.domain.models.CreateRoomInternalRequest;
import org.springframework.stereotype.Component;

import java.util.List;

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
