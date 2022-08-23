package app.groopy.roomservice.infrastructure.elasticsearch.repository.models.entities;

import app.groopy.roomservice.domain.models.common.RoomStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

import static app.groopy.roomservice.domain.elasticsearch.ESIndexes.ROOM_INDEX;

@Data
@Builder
@Document(indexName = ROOM_INDEX)
public class ESRoomEntity {

    @Id
    private String roomId;

    @Field(type = FieldType.Text, name = "roomName")
    private String roomName;

    private List<String> hashtags;

    private List<String> languages;

    @Field(type = FieldType.Text, name = "status")
    private RoomStatus status;

    @GeoPointField
    private GeoPoint location;
}
