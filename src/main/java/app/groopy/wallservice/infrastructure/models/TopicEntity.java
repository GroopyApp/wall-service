package app.groopy.wallservice.infrastructure.models;

import app.groopy.wallservice.domain.models.EventDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("topic")
public class TopicEntity extends Entity {
    private String name;
    private String description;
    private String imageUrl;
    private List<String> categories;
    private String language;
    @Field("associatedEvents")
    private List<EventEntity> events;
    private String chatId;
    private String wallId;
}
