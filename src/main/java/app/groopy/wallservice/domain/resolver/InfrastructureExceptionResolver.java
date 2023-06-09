package app.groopy.wallservice.domain.resolver;

import app.groopy.wallservice.application.exceptions.ApplicationAlreadyExistsException;
import app.groopy.wallservice.application.exceptions.ApplicationBadRequestException;
import app.groopy.wallservice.application.exceptions.ApplicationException;
import app.groopy.wallservice.application.exceptions.ApplicationNotFoundException;
import app.groopy.wallservice.domain.exceptions.*;
import app.groopy.wallservice.domain.models.ErrorMetadataDto;

public class InfrastructureExceptionResolver {

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
        } else if (e instanceof WallNotFoundException) {
            var ex = (WallNotFoundException) e;
            return new ApplicationNotFoundException(ErrorMetadataDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .entityName(ex.getEntityName())
                    .notFoundId(ex.getId())
                    .build());
        } else if (e instanceof UserNotFoundException) {
            var ex = (UserNotFoundException) e;
            return new ApplicationNotFoundException(ErrorMetadataDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .entityName(ex.getEntityName())
                    .notFoundId(ex.getId())
                    .build());
        } else if (e instanceof UserAlreadySubscribedException) {
            var ex = (UserAlreadySubscribedException) e;
            return new ApplicationNotFoundException(ErrorMetadataDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .userId(ex.getUserId())
                    .targetId(ex.getTargetId())
                    .build());
        }

        return new ApplicationException(ErrorMetadataDto.builder()
                .errorDescription("Unmapped exception")
                .build());
    }
}
