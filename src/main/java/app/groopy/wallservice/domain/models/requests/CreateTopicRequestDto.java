package app.groopy.wallservice.domain.models.requests;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CreateTopicRequestDto {
    String wallId;
    String name;
    String description;
    String imageUrl;
    List<String> categories;
    String language;
}
