package app.groopy.wallservice.domain.models.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
public class EventDto extends ThreadableDto {
    String publisher;
    String name;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String eventLocationId;
    String imageUrl;
    ChatInfoDto chatInfo;
    String identifier;
    List<UserLiteDto> participants;
}
