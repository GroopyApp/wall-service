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
    String name;
    String description;
    String imageUrl;
    List<String> categories;
    String language;
    ChatInfo chatInfo;
    @DocumentReference(lazy = true)
    @Field("wall")
    WallEntity wall;
    @DocumentReference
    @Field("associatedEvents")
    List<EventEntity> events;
    @DocumentReference(lazy = true)
    List<UserEntity> subscribers;
}
