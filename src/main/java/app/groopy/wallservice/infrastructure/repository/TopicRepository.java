package app.groopy.wallservice.infrastructure.repository;

import app.groopy.wallservice.infrastructure.models.TopicEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TopicRepository extends MongoRepository<TopicEntity, String> {
    List<TopicEntity> findAllByWallIdEqualsAndCategoriesContainsAndLanguageIn(String wallId, List<String> categories, Collection<String> language);
}
