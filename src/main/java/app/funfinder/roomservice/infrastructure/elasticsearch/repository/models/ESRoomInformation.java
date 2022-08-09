package app.funfinder.roomservice.infrastructure.elasticsearch.repository.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.annotation.Id;

import java.util.List;

import static app.funfinder.roomservice.domain.elasticsearch.ESIndexes.ROOM_INDEX;

@Data
@Builder
@Document(indexName = ROOM_INDEX)
public class ESRoomInformation {

    @Id
    private String roomId;

    @Field(type = FieldType.Text, name = "roomName")
    private String roomName;

    @Field(type = FieldType.Auto, name = "hashtags")
    private List<String> hashtags;

    @Field(type = FieldType.Auto, name = "languages")
    private List<String> languages;

    @GeoPointField
    private String location;
}
