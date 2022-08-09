package app.funfinder.roomservice.presentation.mapper;

import app.funfinder.protobuf.RoomServiceProto;
import app.funfinder.roomservice.domain.models.CreateRoomInternalRequest;
import app.funfinder.roomservice.domain.models.CreateRoomInternalResponse;
import app.funfinder.roomservice.domain.models.ListRoomInternalRequest;
import app.funfinder.roomservice.domain.models.ListRoomInternalResponse;
import app.funfinder.roomservice.domain.models.common.RoomDetails;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {


    @Mappings({
            @Mapping(target = "roomLocation.latitude", source = "latitude"),
            @Mapping(target = "roomLocation.longitude", source = "longitude"),
            @Mapping(target = "creator", source = "userId")})
    CreateRoomInternalRequest map(RoomServiceProto.CreateRoomRequest input);

    @Mappings({
            @Mapping(target = "status", source = "responseStatus"),
            @Mapping(target = "roomId", source = "room.roomId"),
            @Mapping(target = "roomName", source = "room.roomName")})
    RoomServiceProto.CreateRoomResponse map(CreateRoomInternalResponse input);

    @Mappings({
            @Mapping(target = "actualLatitude", source = "latitude"),
            @Mapping(target = "actualLongitude", source = "longitude")})
    ListRoomInternalRequest map(RoomServiceProto.ListRoomRequest input);

    @Mappings({@Mapping(target = "status", source = "responseStatus")})
    RoomServiceProto.ListRoomResponse map(ListRoomInternalResponse input);

    @Mappings({
            @Mapping(target = "id", source = "roomId"),
            @Mapping(target = "name", source = "roomName")})
    RoomServiceProto.Room map(RoomDetails input);
}
