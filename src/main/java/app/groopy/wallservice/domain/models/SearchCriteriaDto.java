package app.groopy.wallservice.domain.models;

import lombok.Value;

import java.util.List;

@Value
public class SearchCriteriaDto {
    LocationDto location;
    List<String> languages;
    List<String> hashtags;
}
