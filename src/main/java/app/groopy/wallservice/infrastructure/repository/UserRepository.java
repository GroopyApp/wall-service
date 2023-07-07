package app.groopy.wallservice.infrastructure.repository;

import app.groopy.wallservice.infrastructure.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);
}
