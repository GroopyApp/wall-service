package app.groopy.wallservice.application.exception;

import app.groopy.wallservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationBadRequestException extends ApplicationException {

    public ApplicationBadRequestException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
