package app.groopy.wallservice.presentation.mapper;

import app.groopy.protobuf.WallServiceProto;
import app.groopy.wallservice.domain.models.*;
import app.groopy.wallservice.domain.models.entities.ChatInfoDto;
import app.groopy.wallservice.domain.models.entities.EventDto;
import app.groopy.wallservice.domain.models.entities.TopicDto;
import app.groopy.wallservice.domain.models.requests.*;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {

    //PROTO to DTO

    CreateTopicRequestDto map(WallServiceProto.CreateTopicRequest input);

    @Mappings({
            @Mapping(target = "locationId", source = "location.locationId")})
    CreateEventRequestDto map(WallServiceProto.CreateEventRequest input);

    SubscribeTopicRequestDto map(WallServiceProto.SubscribeTopicRequest input);

    SubscribeEventRequestDto map(WallServiceProto.SubscribeEventRequest input);

    PublishThreadRequestDto map(WallServiceProto.PublishThreadRequest input);

    @Mappings({@Mapping(target = "onlyFutureEvents", ignore = true)})
    SearchCriteriaDto map(WallServiceProto.SearchCriteria input);

    LocationDto map(WallServiceProto.Location input);

    TopicDto map(WallServiceProto.Topic input);

    @Mappings({
            @Mapping(target = "eventLocationId", source = "location.locationId")})
    EventDto map(WallServiceProto.Event input);


    // DTO to PROTO
    WallServiceProto.Topic map(TopicDto input);

    WallServiceProto.ChatInfo map(ChatInfoDto input);

    @Mappings({
            @Mapping(target = "location.locationId", source = "eventLocationId")
    })
    WallServiceProto.Event map(EventDto input);

    WallServiceProto.ErrorResponse map(ErrorMetadataDto input);

    default LocalDateTime toLocalDate(String input) {
        return LocalDateTime.parse(input);
    }

    default String toDateString(LocalDateTime input) {
        return input.toString();
    }
}
