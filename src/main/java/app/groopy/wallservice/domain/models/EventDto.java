package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDto {
    String name;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer participants;
    String eventLocationId;
    String imageUrl;
    String chatId;
    String identifier;
}
