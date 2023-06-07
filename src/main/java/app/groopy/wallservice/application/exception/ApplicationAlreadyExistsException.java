package app.groopy.wallservice.application.exception;

import app.groopy.wallservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationAlreadyExistsException extends ApplicationException {
    public ApplicationAlreadyExistsException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
