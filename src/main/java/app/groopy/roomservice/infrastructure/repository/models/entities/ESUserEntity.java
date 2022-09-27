package app.groopy.roomservice.infrastructure.repository.models.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

import static app.groopy.roomservice.infrastructure.repository.models.ESIndexes.USER_INDEX;

@Data
@Builder
@Document(indexName = USER_INDEX)
public class ESUserEntity {

    @Id
    private String userId;

    private String name;
    private String surname;
    private List<String> subscribedRooms;
}
