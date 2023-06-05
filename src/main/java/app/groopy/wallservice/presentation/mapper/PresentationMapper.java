package app.groopy.wallservice.presentation.mapper;

import app.groopy.protobuf.WallServiceProto;
import app.groopy.wallservice.domain.models.*;
import com.google.protobuf.Timestamp;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {

    //PROTO to DTO

    SearchCriteriaDto map(WallServiceProto.SearchCriteria input);

    LocationDto map(WallServiceProto.Location input);

    TopicDto map(WallServiceProto.Topic input);

    @Mappings({
            @Mapping(target = "eventLocationId", source = "location.locationId")})
    EventDto map(WallServiceProto.Event input);


    // DTO to PROTO
    WallServiceProto.Topic map(TopicDto input);

    WallServiceProto.Event map(EventDto input);

    WallServiceProto.GetWallResponse map(WallResponseDto input);


    default LocalDateTime toLocalDate(String input) {
        return LocalDateTime.parse(input);
    }

    default String toDateString(LocalDateTime input) {
        return input.toString();
    }


//    WallServiceProto.Topic map(TopicDto input);

//    @Mappings({
//            @Mapping(target = "actualLatitude", source = "latitude"),
//            @Mapping(target = "actualLongitude", source = "longitude")})
//    ListRoomRequestDto map(WallServiceProto.ListRoomRequest input);
//
//    WallServiceProto.ListRoomResponse map(ListRoomResponseDto input);
//
//    WallServiceProto.SubscribeRoomResponse map(SubscribeResponseDto input);
//
//    @Mappings({
//            @Mapping(target = "id", source = "wallId"),
//            @Mapping(target = "name", source = "wallName")})
//    WallServiceProto.Room map(RoomDetailsDto input);
}
