package app.groopy.wallservice.domain.exceptions;

import app.groopy.wallservice.application.exception.ApplicationAlreadyExistsException;
import app.groopy.wallservice.application.exception.ApplicationBadRequestException;
import app.groopy.wallservice.application.exception.ApplicationException;
import app.groopy.wallservice.application.exception.ApplicationNotFoundException;
import app.groopy.wallservice.domain.models.ErrorMetadataDto;

public class ApplicationExceptionResolver {

    public static ApplicationException resolve(Exception e) {
        if (e instanceof EntityAlreadyExistsException) {
            var ex = (EntityAlreadyExistsException) e;
            return new ApplicationAlreadyExistsException(ErrorMetadataDto.builder()
                    .errorDescription(ex.getLocalizedMessage())
                    .existingEntityId(ex.getId())
                    .entityName(ex.getEntityName())
                    .build());
        } else if (e instanceof TopicNotFoundException) {
            var ex = (TopicNotFoundException) e;
            return new ApplicationNotFoundException(ErrorMetadataDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .entityName(ex.getEntityName())
                    .notFoundId(ex.getId())
                    .build());
        } else if (e instanceof EndDateIsBeforeException) {
            var ex = (EndDateIsBeforeException) e;
            return new ApplicationBadRequestException(ErrorMetadataDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .startDate(ex.getStartDate().toString())
                    .endDate(ex.getEndDate().toString())
                    .build());
        } else if (e instanceof EventInThePastException) {
            var ex = (EventInThePastException) e;
            return new ApplicationBadRequestException(ErrorMetadataDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .startDate(ex.getStartDate().toString())
                    .build());
        } else if(e instanceof WallNotFoundException) {
            var ex = (WallNotFoundException) e;
            new ApplicationNotFoundException(ErrorMetadataDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .entityName(ex.getEntityName())
                    .notFoundId(ex.getLocationId())
                    .build());
        }

        return new ApplicationException(ErrorMetadataDto.builder()
                .errorDescription("unknown error")
                .build());
    }
}
