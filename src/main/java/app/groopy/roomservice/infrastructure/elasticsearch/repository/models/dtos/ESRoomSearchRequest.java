package app.groopy.roomservice.infrastructure.elasticsearch.repository.models.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ESRoomSearchRequest {

    private ESPoint point;
    private List<String> hashtags;
    private List<String> languages;
}
