package app.groopy.wallservice.domain.services;

import app.groopy.wallservice.application.mapper.ApplicationMapper;
import app.groopy.wallservice.domain.exceptions.EndDateIsBeforeException;
import app.groopy.wallservice.domain.exceptions.EntityAlreadyExistsException;
import app.groopy.wallservice.domain.exceptions.WallNotFoundException;
import app.groopy.wallservice.domain.exceptions.TopicNotFoundException;
import app.groopy.wallservice.domain.models.CreateEventRequestDto;
import app.groopy.wallservice.domain.models.CreateTopicRequestDto;
import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.domain.models.TopicDto;
import app.groopy.wallservice.domain.utils.UUIDUtils;
import app.groopy.wallservice.infrastructure.models.EventEntity;
import app.groopy.wallservice.infrastructure.models.TopicEntity;
import app.groopy.wallservice.infrastructure.models.WallEntity;
import app.groopy.wallservice.infrastructure.repository.TopicRepository;
import app.groopy.wallservice.infrastructure.repository.WallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudService.class);

    private final TopicRepository topicRepository;
    private final WallRepository wallRepository;
    private final MongoTemplate mongoTemplate;

    private final Validator validator;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public CrudService(TopicRepository topicRepository, WallRepository wallRepository, MongoTemplate mongoTemplate, Validator validator, ApplicationMapper applicationMapper) {
        this.topicRepository = topicRepository;
        this.wallRepository = wallRepository;
        this.mongoTemplate = mongoTemplate;
        this.validator = validator;
        this.applicationMapper = applicationMapper;
    }

    public List<TopicDto> getTopicsBy(SearchCriteriaDto requestCriteria) throws WallNotFoundException {
        Optional<WallEntity> wall = wallRepository.findByLocationId(requestCriteria.getLocation().getLocationId());
        return wall.map(wallEntity -> topicRepository.findByCriteria(
                mongoTemplate,
                wallEntity.getId(),
                requestCriteria.getHashtags(),
                requestCriteria.getLanguages()).stream().map(applicationMapper::map).toList())
                .orElseThrow(() -> new WallNotFoundException(requestCriteria.getLocation().getLocationId()));
    }

    public TopicDto createTopic(CreateTopicRequestDto createTopicRequest) throws EntityAlreadyExistsException {
        var identifier = UUIDUtils.generateUUID(createTopicRequest);
        validator.validate(identifier, TopicEntity.class);
        TopicEntity topic = topicRepository.save(TopicEntity.builder()
                .wallId(createTopicRequest.getWallId())
                .name(createTopicRequest.getName())
                .description(createTopicRequest.getDescription())
                .imageUrl(createTopicRequest.getImageUrl())
                .categories(createTopicRequest.getCategories())
                .language(createTopicRequest.getLanguage())
                .events(new ArrayList<>())
                .chatId(identifier)
                .build());
        return applicationMapper.map(topic);
    }

    public TopicDto createEvent(CreateEventRequestDto createEventRequest) throws EntityAlreadyExistsException, TopicNotFoundException, EndDateIsBeforeException {
        validator.validate(createEventRequest);

        TopicEntity topic = topicRepository.findById(createEventRequest.getTopicId())
                .orElseThrow(() -> new TopicNotFoundException(createEventRequest.getTopicId()));

        var identifier = UUIDUtils.generateUUID(createEventRequest);
        validator.validate(identifier, topic.getEvents());
        topic.getEvents().add(EventEntity.builder()
                        .identifier(identifier)
                        .chatId(identifier)
                        .eventLocationId(createEventRequest.getLocationId())
                        .imageUrl(createEventRequest.getImageUrl())
                        .description(createEventRequest.getDescription())
                        .name(createEventRequest.getName())
                        .startDate(createEventRequest.getStartDate())
                        .endDate(createEventRequest.getEndDate())
                        .participants(0)
                .build());
        topic = topicRepository.save(topic);
        return applicationMapper.map(topic);
    }
}
