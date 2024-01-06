package app.groopy.wallservice.domain.models.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class TopicDto extends ThreadableDto {
    String publisher;
    String name;
    String description;
    String imageUrl;
    List<String> categories;
    String language;
    List<EventDto> events;
    ChatInfoDto chatInfo;
    List<UserLiteDto> subscribers;
}
