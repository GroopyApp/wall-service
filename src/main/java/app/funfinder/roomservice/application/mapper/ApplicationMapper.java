package app.funfinder.roomservice.application.mapper;


import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.entities.ESRoomEntity;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    @Mappings({
            @Mapping(target = "latitude", source = "location.lat"),
            @Mapping(target = "longitude", source = "location.lon")})
    RoomDetails map(ESRoomEntity input);
}
