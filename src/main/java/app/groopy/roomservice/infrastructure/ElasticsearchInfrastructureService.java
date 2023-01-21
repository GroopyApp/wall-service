package app.groopy.roomservice.infrastructure;

import app.groopy.commons.domain.models.RoomStatus;
import app.groopy.commons.infrastructure.providers.ElasticsearchProvider;
import app.groopy.commons.infrastructure.providers.exceptions.ElasticsearchProviderException;
import app.groopy.commons.infrastructure.repository.models.InternalGeoPoint;
import app.groopy.commons.infrastructure.repository.models.RoomSearchRequest;
import app.groopy.commons.infrastructure.repository.models.SearchScope;
import app.groopy.commons.infrastructure.repository.models.entities.RoomEntity;
import app.groopy.roomservice.domain.exceptions.RoomNotFoundException;
import app.groopy.roomservice.domain.exceptions.SubscribeToRoomException;
import app.groopy.roomservice.domain.models.CreateRoomRequestDto;
import app.groopy.roomservice.domain.models.ListRoomRequestDto;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ElasticsearchInfrastructureService {

    private final ElasticsearchProvider provider;

    public ElasticsearchInfrastructureService(ElasticsearchProvider provider) {
        this.provider = provider;
    }

    @SneakyThrows
    public RoomEntity subscribeUserToRoom(String userId, String roomId) {
        try {
            return provider.subscribeUserToRoom(userId, roomId);
        } catch (ElasticsearchProviderException e) {
            throw new SubscribeToRoomException(userId, roomId);
        }
    }

    public List<RoomEntity> findBySearchRequest(ListRoomRequestDto request, SearchScope searchScope) {
        return provider.findBySearchRequest(RoomSearchRequest.builder()
                .point(InternalGeoPoint.builder()
                        .latitude(request.getActualLatitude())
                        .longitude(request.getActualLongitude())
                        .distanceAvailability(request.getSearchRangeInMeters())
                        .build())
                .hashtags(request.getHashtags())
                .languages(request.getLanguages())
                .build(), searchScope);
    }

    @SneakyThrows
    public List<RoomEntity> findByUserId(String userId) {
        try {
            return provider.findByUserId(userId);
        } catch (ElasticsearchProviderException e) {
            //FIXME add field variables into the common exception and do a switch case to return the correct exception in here
            throw new RoomNotFoundException(e.getLocalizedMessage());
        }
    }

    public RoomEntity storeRoom(String roomId, CreateRoomRequestDto request) {
        return provider.save(RoomEntity.builder()
                .roomId(roomId)
                .roomName(request.getRoomName())
                .hashtags(request.getHashtags())
                .languages(request.getLanguages())
//              .status(RoomStatus.PENDING) TODO implement this and remove line below
                .status(RoomStatus.CREATED)
                .location(new GeoPoint(
                        request.getRoomLocation().getLatitude(),
                        request.getRoomLocation().getLongitude()))
                .build());
    }
}
