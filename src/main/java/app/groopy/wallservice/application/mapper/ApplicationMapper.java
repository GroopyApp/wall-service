package app.groopy.wallservice.application.mapper;

import app.groopy.wallservice.domain.models.entities.*;
import app.groopy.wallservice.infrastructure.models.*;
import org.mapstruct.*;


@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    @Mappings({
            @Mapping(target = "subscribers", source = "subscribers", qualifiedByName = "liteUser"),
            @Mapping(target = "threads", source = "threads", qualifiedByName = "mapId")})
    TopicDto map(TopicEntity input);

    @Mappings({
            @Mapping(target = "participants", source = "participants", qualifiedByName = "liteUser"),
            @Mapping(target = "threads", source = "threads", qualifiedByName = "mapId")})
    EventDto map(EventEntity input);

    @Mappings({
            @Mapping(target = "channelName", source = "chatName")})
    ChatInfoDto map(ChatInfo input);

    @Mappings({
            @Mapping(target = "subscribedTopics", source = "subscribedTopics", qualifiedByName = "mapId"),
            @Mapping(target = "subscribedEvents", source = "subscribedEvents", qualifiedByName = "mapId")})
    UserDto map(UserEntity input);

    @Named("liteUser")
    UserLiteDto liteMap(UserEntity input);

    @Named("mapId")
    default String mapId(Entity entity) {
        return entity.getId();
    }
}
