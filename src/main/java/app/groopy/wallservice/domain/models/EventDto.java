package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventDto {
    String name;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String eventLocationId;
    String imageUrl;
    String chatId;
    String identifier;
    List<UserLiteDto> participants;
}
