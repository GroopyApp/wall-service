package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateEventRequestDto {
    String topicId;
    String name;
    String description;
    String locationId;
    String imageUrl;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
