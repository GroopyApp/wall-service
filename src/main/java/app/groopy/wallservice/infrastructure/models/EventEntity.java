package app.groopy.wallservice.infrastructure.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("events")
public class EventEntity extends ThreadableEntity {
    String name;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String eventLocationId;
    String imageUrl;
    ChatInfo chatInfo;
    String identifier;
    @DocumentReference(lazy = true)
    TopicEntity topic;
    @DocumentReference(lazy = true)
    List<UserEntity> participants;
}
