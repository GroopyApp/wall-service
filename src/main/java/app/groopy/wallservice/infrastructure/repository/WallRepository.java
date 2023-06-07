package app.groopy.wallservice.infrastructure.repository;

import app.groopy.wallservice.infrastructure.models.WallEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WallRepository extends MongoRepository<WallEntity, String> {
    Optional<WallEntity> findByLocationId(String locationId);
}
