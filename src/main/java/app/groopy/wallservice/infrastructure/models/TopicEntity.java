package app.groopy.wallservice.infrastructure.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
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
    private String imageUrl;
    private List<String> categories;
    private String language;
    private String chatId;
    @DocumentReference(lazy = true)
    @Field("wall")
    private WallEntity wall;
    @DocumentReference
    @Field("associatedEvents")
    private List<EventEntity> events;
    @DocumentReference(lazy = true)
    List<UserEntity> subscribers;
}
