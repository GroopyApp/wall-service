package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class SearchCriteriaDto {
    LocationDto location;
    List<String> languages;
    List<String> hashtags;
    String userId;
    boolean onlyFutureEvents;
}
