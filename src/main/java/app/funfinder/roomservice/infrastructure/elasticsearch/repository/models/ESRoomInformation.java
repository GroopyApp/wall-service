package app.funfinder.roomservice.infrastructure.elasticsearch.repository.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.geo.Point;

import java.util.List;

@Data
@Builder
@Document(indexName = "room_info")
public class ESRoomInformation {

    @Id
    private String roomId;

    @Field(type = FieldType.Text, name = "roomName")
    private String roomName;

    @Field(type = FieldType.Object, name = "point")
    private Point point;

    @Field(type = FieldType.Keyword, name = "hashtags")
    private List<String> hashtags;

    @Field(type = FieldType.Keyword, name = "languages")
    private List<String> languages;
}
