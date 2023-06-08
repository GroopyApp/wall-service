package app.groopy.wallservice.application.mapper;

import app.groopy.wallservice.domain.models.EventDto;
import app.groopy.wallservice.domain.models.TopicDto;
import app.groopy.wallservice.domain.models.UserDto;
import app.groopy.wallservice.domain.models.UserLiteDto;
import app.groopy.wallservice.infrastructure.models.Entity;
import app.groopy.wallservice.infrastructure.models.EventEntity;
import app.groopy.wallservice.infrastructure.models.TopicEntity;
import app.groopy.wallservice.infrastructure.models.UserEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    @Mappings({
            @Mapping(target = "subscribers", source = "subscribers", qualifiedByName = "liteUser")})
    TopicDto map(TopicEntity input);

    @Mappings({
            @Mapping(target = "participants", source = "participants", qualifiedByName = "liteUser")})
    EventDto map(EventEntity input);

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
