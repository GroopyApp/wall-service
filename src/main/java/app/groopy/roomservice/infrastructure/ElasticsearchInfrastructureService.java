package app.groopy.roomservice.infrastructure;

import app.groopy.providers.elasticsearch.models.RoomStatus;
import app.groopy.providers.elasticsearch.ElasticsearchProvider;
import app.groopy.providers.elasticsearch.exceptions.ElasticsearchProviderException;
import app.groopy.providers.elasticsearch.models.InternalGeoPoint;
import app.groopy.providers.elasticsearch.models.RoomSearchRequest;
import app.groopy.providers.elasticsearch.models.SearchScope;
import app.groopy.providers.elasticsearch.models.entities.RoomEntity;
import app.groopy.roomservice.domain.models.CreateRoomRequestDto;
import app.groopy.roomservice.domain.models.ListRoomRequestDto;
import app.groopy.roomservice.infrastructure.exceptions.ElasticsearchServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElasticsearchInfrastructureService {

    private final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchInfrastructureService.class);

    private final ElasticsearchProvider provider;

    public ElasticsearchInfrastructureService(ElasticsearchProvider provider) {
        this.provider = provider;
    }

    public RoomEntity subscribeUserToRoom(String userId, String roomId) throws ElasticsearchServiceException {
        try {
            return provider.subscribeUserToRoom(userId, roomId);
        } catch (ElasticsearchProviderException e) {
            LOGGER.error("An error occurred trying to call elasticsearch", e);
            throw new ElasticsearchServiceException(String.format("Unable subscribe user with id %s to room %s", userId, roomId), e.getError());
        }
    }

    public List<RoomEntity> findBySearchRequest(ListRoomRequestDto request, SearchScope searchScope) throws ElasticsearchServiceException {
        try {
            return provider.findBySearchRequest(RoomSearchRequest.builder()
                    .point(InternalGeoPoint.builder()
                            .latitude(request.getActualLatitude())
                            .longitude(request.getActualLongitude())
                            .distanceAvailability(request.getSearchRangeInMeters())
                            .build())
                    .hashtags(request.getHashtags())
                    .languages(request.getLanguages())
                    .build(), searchScope);
        } catch (ElasticsearchProviderException e) {
            LOGGER.error("An error occurred trying to call elasticsearch", e);
            throw new ElasticsearchServiceException(String.format("Unable find rooms in search scope %s with given search request %s", searchScope, request.toString()), e.getError());
        }
    }

    public List<RoomEntity> findByUserId(String userId) throws ElasticsearchServiceException {
        try {
            return provider.findByUserId(userId);
        } catch (ElasticsearchProviderException e) {
            LOGGER.error("An error occurred trying to call elasticsearch", e);
            throw new ElasticsearchServiceException(String.format("Unable find user with id %s", userId), e.getError());
        }
    }

    public RoomEntity storeRoom(String roomId, CreateRoomRequestDto request) throws ElasticsearchServiceException {
        try {
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
        } catch (ElasticsearchProviderException e) {
            LOGGER.error("An error occurred trying to call elasticsearch", e);
            throw new ElasticsearchServiceException(String.format("Unable store new room with id %s and given request %s", roomId, request), e.getError());
        }
    }
}
