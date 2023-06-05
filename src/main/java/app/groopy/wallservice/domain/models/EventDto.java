package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDto {
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer participants;
    String eventLocationId;
    String chatId;
}
