package app.groopy.roomservice.presentation.mapper;

import app.groopy.protobuf.RoomServiceProto;
import app.groopy.roomservice.domain.models.*;
import app.groopy.roomservice.domain.models.common.RoomDetailsDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {


    @Mappings({
            @Mapping(target = "roomLocation.latitude", source = "latitude"),
            @Mapping(target = "roomLocation.longitude", source = "longitude"),
            @Mapping(target = "creator", source = "userId")})
    CreateRoomRequestDto map(RoomServiceProto.CreateRoomRequest input);

    RoomServiceProto.CreateRoomResponse map(CreateRoomResponseDto input);

    @Mappings({
            @Mapping(target = "actualLatitude", source = "latitude"),
            @Mapping(target = "actualLongitude", source = "longitude")})
    ListRoomRequestDto map(RoomServiceProto.ListRoomRequest input);

    RoomServiceProto.ListRoomResponse map(ListRoomResponseDto input);

    RoomServiceProto.SubscribeRoomResponse map(SubscribeResponseDto input);

    @Mappings({
            @Mapping(target = "id", source = "roomId"),
            @Mapping(target = "name", source = "roomName")})
    RoomServiceProto.Room map(RoomDetailsDTO input);
}
