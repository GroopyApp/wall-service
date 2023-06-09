package app.groopy.wallservice.application.exceptions;

import app.groopy.wallservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationException extends Exception {
    private final ErrorMetadataDto errorResponse;

    public ApplicationException(ErrorMetadataDto errorResponse) {
        this.errorResponse = errorResponse;
    }
}
