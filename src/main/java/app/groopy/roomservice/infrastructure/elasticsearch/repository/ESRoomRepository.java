package app.groopy.roomservice.infrastructure.elasticsearch.repository;

import app.groopy.roomservice.infrastructure.elasticsearch.repository.models.entities.ESRoomEntity;
import app.groopy.roomservice.domain.models.common.RoomStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ESRoomRepository extends ElasticsearchRepository<ESRoomEntity, String> {

    List<ESRoomEntity> getESRoomEntitiesByRoomIdInAndStatusIn(List<String> roomIds, List<RoomStatus> statuses);
    Optional<ESRoomEntity> findByRoomName(String roomName);

}