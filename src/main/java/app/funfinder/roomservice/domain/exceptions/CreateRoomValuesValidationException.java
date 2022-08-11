package app.funfinder.roomservice.domain.exceptions;

public class CreateRoomValuesValidationException extends Throwable {

    public CreateRoomValuesValidationException(String value, String fieldName) {
        super(value + " is not a valid value for " + fieldName);
    }
}
