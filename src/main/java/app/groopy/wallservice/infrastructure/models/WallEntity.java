package app.groopy.wallservice.infrastructure.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Document("wall")
public class WallEntity extends Entity {

    private String locationId;
    @DocumentReference(lazy = true)
    List<TopicEntity> topics;
}
