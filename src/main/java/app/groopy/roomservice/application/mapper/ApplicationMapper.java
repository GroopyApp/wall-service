package app.groopy.roomservice.application.mapper;

import app.groopy.providers.elasticsearch.models.entities.RoomEntity;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    @Mappings({
            @Mapping(target = "latitude", source = "location.lat"),
            @Mapping(target = "longitude", source = "location.lon")})
    RoomDetailsDto map(RoomEntity input);
}
