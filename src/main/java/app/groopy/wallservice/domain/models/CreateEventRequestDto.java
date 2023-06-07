package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateEventRequestDto {
    String topicId;
    String description;
    String locationId;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
