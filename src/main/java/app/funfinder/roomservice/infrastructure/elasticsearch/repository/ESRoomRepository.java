package app.funfinder.roomservice.infrastructure.elasticsearch.repository;

import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.entities.ESRoomEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ESRoomRepository extends ElasticsearchRepository<ESRoomEntity, String> {}