package app.groopy.wallservice.application.exception;

import app.groopy.wallservice.domain.models.ErrorDto;
import lombok.Getter;

@Getter
public class ApplicationException extends Exception {
    private final ErrorDto errorResponse;

    public ApplicationException(ErrorDto errorResponse) {
        this.errorResponse = errorResponse;
    }
}
