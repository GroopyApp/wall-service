package app.groopy.wallservice.infrastructure.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("users")
public class UserEntity extends Entity {
    private String userId;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String gender;
    private String language;
    private String photoUrl;
    @DocumentReference(lazy = true)
    private List<TopicEntity> subscribedTopics;
    @DocumentReference(lazy = true)
    private List<EventEntity> subscribedEvents;
}
