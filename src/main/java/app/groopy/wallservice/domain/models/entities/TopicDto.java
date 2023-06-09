package app.groopy.wallservice.domain.models.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class TopicDto extends BasicEntityDto {
    private String name;
    private String description;
    private String imageUrl;
    private List<String> categories;
    private String language;
    private List<EventDto> events;
    private String chatId;
    private List<UserLiteDto> subscribers;
}
