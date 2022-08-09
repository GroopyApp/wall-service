package app.funfinder.roomservice.application.mapper;


import app.funfinder.roomservice.domain.models.common.RoomDetails;
import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomInformation;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    @Mappings({
            @Mapping(target = "latitude", ignore = true),
            @Mapping(target = "longitude", ignore = true)})
    RoomDetails map(ESRoomInformation input);
}
