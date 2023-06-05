package app.groopy.wallservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventEntity {
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Integer participants;
    String eventLocationId;
    String chatId;
}
