package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    String entityName;
    String existingEntityId;
    String errorDescription;
}
