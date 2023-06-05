package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TopicDto {
    private String id;
    private String name;
    private String description;
    private List<String> categories;
    private String chatId;
    private List<EventDto> events;
}
