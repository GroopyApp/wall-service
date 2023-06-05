package app.groopy.wallservice.presentation.mapper;

import app.groopy.protobuf.WallServiceProto;
import app.groopy.wallservice.domain.models.*;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {

    //PROTO to DTO

    CreateTopicRequestDto map(WallServiceProto.CreateTopicRequest input);

    SearchCriteriaDto map(WallServiceProto.SearchCriteria input);

    LocationDto map(WallServiceProto.Location input);

    TopicDto map(WallServiceProto.Topic input);

    @Mappings({
            @Mapping(target = "eventLocationId", source = "location.locationId")})
    EventDto map(WallServiceProto.Event input);


    // DTO to PROTO
    WallServiceProto.Topic map(TopicDto input);

    WallServiceProto.Event map(EventDto input);

    WallServiceProto.ErrorResponse map(ErrorDto input);

    default LocalDateTime toLocalDate(String input) {
        return LocalDateTime.parse(input);
    }

    default String toDateString(LocalDateTime input) {
        return input.toString();
    }
}
