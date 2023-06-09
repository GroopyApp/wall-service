package app.groopy.wallservice.application.exceptions;

import app.groopy.wallservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationBadRequestException extends ApplicationException {

    public ApplicationBadRequestException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
