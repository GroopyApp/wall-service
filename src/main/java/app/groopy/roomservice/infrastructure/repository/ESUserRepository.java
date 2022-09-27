package app.groopy.roomservice.infrastructure.repository;

import app.groopy.roomservice.infrastructure.repository.models.entities.ESUserEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ESUserRepository extends ElasticsearchRepository<ESUserEntity, String> {

}