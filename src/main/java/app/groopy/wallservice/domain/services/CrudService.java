package app.groopy.wallservice.domain.services;

import app.groopy.wallservice.application.mapper.ApplicationMapper;
import app.groopy.wallservice.domain.exceptions.EntityAlreadyExistsException;
import app.groopy.wallservice.domain.models.CreateTopicRequestDto;
import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.domain.models.TopicDto;
import app.groopy.wallservice.domain.utils.UUIDUtils;
import app.groopy.wallservice.infrastructure.models.TopicEntity;
import app.groopy.wallservice.infrastructure.models.WallEntity;
import app.groopy.wallservice.infrastructure.repository.TopicRepository;
import app.groopy.wallservice.infrastructure.repository.WallRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudService.class);

    private final TopicRepository topicRepository;
    private final WallRepository wallRepository;

    private final Validator validator;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public CrudService(TopicRepository topicRepository, WallRepository wallRepository, Validator validator, ApplicationMapper applicationMapper) {
        this.topicRepository = topicRepository;
        this.wallRepository = wallRepository;
        this.validator = validator;
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

    public TopicDto createTopic(CreateTopicRequestDto createTopicRequest) throws EntityAlreadyExistsException {
        var identifier = UUIDUtils.generateUUID(createTopicRequest);
        validator.validate(identifier, TopicEntity.class);
        TopicEntity topic = topicRepository.save(TopicEntity.builder()
                .wallId(createTopicRequest.getWallId())
                .name(createTopicRequest.getName())
                .description(createTopicRequest.getDescription())
                .language(createTopicRequest.getLanguage())
                .categories(createTopicRequest.getCategories())
                .chatId(identifier)
                .build());
        return applicationMapper.map(topic);
    }
}
