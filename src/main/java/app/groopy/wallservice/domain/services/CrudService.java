package app.groopy.wallservice.domain.services;

import app.groopy.wallservice.application.mapper.ApplicationMapper;
import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.domain.models.TopicDto;
import app.groopy.wallservice.infrastructure.models.TopicEntity;
import app.groopy.wallservice.infrastructure.models.WallEntity;
import app.groopy.wallservice.infrastructure.repository.TopicRepository;
import app.groopy.wallservice.infrastructure.repository.WallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CrudService {

    private final TopicRepository topicRepository;
    private final WallRepository wallRepository;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public CrudService(TopicRepository topicRepository, WallRepository wallRepository, ApplicationMapper applicationMapper) {
        this.topicRepository = topicRepository;
        this.wallRepository = wallRepository;
        this.applicationMapper = applicationMapper;
    }

    public List<TopicDto> getTopicsBy(SearchCriteriaDto requestCriteria) {

        Optional<WallEntity> wall = wallRepository.findByLocationId(requestCriteria.getLocation().getLocationId());

        if (wall.isPresent()) {
            List<TopicDto> topics = applicationMapper.map(
                    topicRepository.findAllByWallIdEqualsAndCategoriesContainsAndLanguageIn(
                            wall.get().getId(),
                            requestCriteria.getHashtags(),
                            requestCriteria.getLanguages())
            );
            return topics;
        } else {
            return null;
        }
    }
}
