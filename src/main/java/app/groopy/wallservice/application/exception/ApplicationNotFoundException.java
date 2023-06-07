package app.groopy.wallservice.application.exception;

import app.groopy.wallservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationNotFoundException extends ApplicationException {
    public ApplicationNotFoundException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
