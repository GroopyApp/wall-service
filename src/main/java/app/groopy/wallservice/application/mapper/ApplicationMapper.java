package app.groopy.wallservice.application.mapper;

import app.groopy.wallservice.domain.models.EventDto;
import app.groopy.wallservice.domain.models.TopicDto;
import app.groopy.wallservice.infrastructure.models.EventEntity;
import app.groopy.wallservice.infrastructure.models.TopicEntity;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {
    
    TopicDto map(TopicEntity input);

    EventDto map(EventEntity input);

//    @Mappings({
//            @Mapping(target = "latitude", source = "location.lat"),
//            @Mapping(target = "longitude", source = "location.lon")})
//    RoomDetailsDto map(RoomEntity input);
}
