package app.groopy.roomservice.presentation.mapper;

import app.groopy.protobuf.RoomServiceProto;
import app.groopy.roomservice.domain.models.*;
import app.groopy.roomservice.domain.models.common.RoomDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {


    @Mappings({
            @Mapping(target = "roomLocation.latitude", source = "latitude"),
            @Mapping(target = "roomLocation.longitude", source = "longitude"),
            @Mapping(target = "creator", source = "userId")})
    CreateRoomInternalRequest map(RoomServiceProto.CreateRoomRequest input);

    RoomServiceProto.CreateRoomResponse map(CreateRoomInternalResponse input);

    @Mappings({
            @Mapping(target = "actualLatitude", source = "latitude"),
            @Mapping(target = "actualLongitude", source = "longitude")})
    ListRoomInternalRequest map(RoomServiceProto.ListRoomRequest input);

    RoomServiceProto.ListRoomResponse map(ListRoomInternalResponse input);

    RoomServiceProto.SubscribeRoomResponse map(SubscribeInternalResponse input);

    @Mappings({
            @Mapping(target = "id", source = "roomId"),
            @Mapping(target = "name", source = "roomName")})
    RoomServiceProto.Room map(RoomDetails input);
}
