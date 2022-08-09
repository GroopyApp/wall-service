package app.funfinder.roomservice.infrastructure.elasticsearch.repository;

import app.funfinder.roomservice.infrastructure.elasticsearch.repository.models.ESRoomInformation;
import org.elasticsearch.common.unit.DistanceUnit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ESRoomRepository extends ElasticsearchRepository<ESRoomInformation, String> {

    List<ESRoomInformation> findByLocationNear(@Param("location") GeoPoint location, @Param("distance") DistanceUnit.Distance distance);
}
