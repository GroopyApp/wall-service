package app.groopy.wallservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("topic")
public class TopicEntity extends Entity {
    private String name;
    private String description;
    private List<String> categories;
    private String language;
    private String chatId;
    @Field("associatedEvents")
    private List<EventEntity> events;
    private String wallId;
}
