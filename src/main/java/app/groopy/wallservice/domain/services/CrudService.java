package app.groopy.wallservice.domain.services;

import app.groopy.wallservice.application.mapper.ApplicationMapper;
import app.groopy.wallservice.domain.exceptions.*;
import app.groopy.wallservice.domain.models.*;
import app.groopy.wallservice.domain.utils.UUIDUtils;
import app.groopy.wallservice.infrastructure.models.EventEntity;
import app.groopy.wallservice.infrastructure.models.TopicEntity;
import app.groopy.wallservice.infrastructure.models.UserEntity;
import app.groopy.wallservice.infrastructure.models.WallEntity;
import app.groopy.wallservice.infrastructure.repository.EventRepository;
import app.groopy.wallservice.infrastructure.repository.TopicRepository;
import app.groopy.wallservice.infrastructure.repository.UserRepository;
import app.groopy.wallservice.infrastructure.repository.WallRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class CrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudService.class);

    private final TopicRepository topicRepository;
    private final EventRepository eventRepository;
    private final WallRepository wallRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    private final Validator validator;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public CrudService(
            TopicRepository topicRepository,
            EventRepository eventRepository,
            WallRepository wallRepository,
            UserRepository userRepository,
            MongoTemplate mongoTemplate,
            Validator validator,
            ApplicationMapper applicationMapper) {
        this.topicRepository = topicRepository;
        this.eventRepository = eventRepository;
        this.wallRepository = wallRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.validator = validator;
        this.applicationMapper = applicationMapper;
    }

    @SneakyThrows
    public List<TopicDto> getTopicsBy(SearchCriteriaDto requestCriteria) {
        WallEntity wall = wallRepository.findByLocationId(requestCriteria.getLocation().getLocationId())
                .orElseThrow(() -> new WallNotFoundException(requestCriteria.getLocation().getLocationId(), WallNotFoundException.WallSearchType.LOCATION_ID));

        return topicRepository.findByCriteria(
                mongoTemplate,
                wall.getId(),
                requestCriteria.getHashtags(),
                requestCriteria.getLanguages(),
                requestCriteria.isOnlyFutureEvents()).stream().map(applicationMapper::map).toList();
    }

    @SneakyThrows
    public TopicDto createTopic(CreateTopicRequestDto createTopicRequest) {
        var identifier = UUIDUtils.generateUUID(createTopicRequest);
        validator.validate(identifier, TopicEntity.class);
        WallEntity wall = wallRepository.findById(createTopicRequest.getWallId())
                .orElseThrow(() -> new WallNotFoundException(createTopicRequest.getWallId(), WallNotFoundException.WallSearchType.ID));
        TopicEntity topic = topicRepository.save(TopicEntity.builder()
                .wall(wall)
                .name(createTopicRequest.getName())
                .description(createTopicRequest.getDescription())
                .imageUrl(createTopicRequest.getImageUrl())
                .categories(createTopicRequest.getCategories())
                .language(createTopicRequest.getLanguage())
                .events(new ArrayList<>())
                .chatId(identifier)
                .build());
        wall.getTopics().add(topic);
        wallRepository.save(wall);
        return applicationMapper.map(topic);
    }

    @SneakyThrows
    public TopicDto createEvent(CreateEventRequestDto createEventRequest) {
        validator.validate(createEventRequest);

        TopicEntity topic = topicRepository.findById(createEventRequest.getTopicId())
                .orElseThrow(() -> new TopicNotFoundException(createEventRequest.getTopicId()));

        var identifier = UUIDUtils.generateUUID(createEventRequest);
        validator.validate(identifier, topic.getEvents());
        EventEntity event = eventRepository.save(EventEntity.builder()
                        .topic(topic)
                        .identifier(identifier)
                        .chatId(identifier)
                        .eventLocationId(createEventRequest.getLocationId())
                        .imageUrl(createEventRequest.getImageUrl())
                        .description(createEventRequest.getDescription())
                        .name(createEventRequest.getName())
                        .startDate(createEventRequest.getStartDate())
                        .endDate(createEventRequest.getEndDate())
                        .participants(new ArrayList<>())
                .build());
        topic.getEvents().add(event);
        topic = topicRepository.save(topic);
        return applicationMapper.map(topic);
    }

    @SneakyThrows
    public TopicDto subscribeTopic(SubscribeTopicRequestDto subscribeTopicRequest) {
        TopicEntity topic = topicRepository.findById(subscribeTopicRequest.getTopicId())
                .orElseThrow(() -> new TopicNotFoundException(subscribeTopicRequest.getTopicId()));
        UserEntity user = userRepository.findByUserId(subscribeTopicRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(subscribeTopicRequest.getUserId()));
        if (topic.getSubscribers().stream().anyMatch(userEntity -> userEntity.getId().equals(subscribeTopicRequest.getUserId()))) {
            throw new UserAlreadySubscribedException(
                    subscribeTopicRequest.getUserId(),
                    subscribeTopicRequest.getTopicId(),
                    UserAlreadySubscribedException.SubscriptionType.TOPIC);
        }

        topic.getSubscribers().add(user);
        user.getSubscribedTopics().add(topic);

        userRepository.save(user);
        return applicationMapper.map(topicRepository.save(topic));
    }

    @SneakyThrows
    public EventDto subscribeEvent(SubscribeEventRequestDto subscribeEventRequest) {
        EventEntity event = eventRepository.findById(subscribeEventRequest.getEventId())
                .orElseThrow(() -> new EventNotFoundException(subscribeEventRequest.getEventId()));
        UserEntity user = userRepository.findByUserId(subscribeEventRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(subscribeEventRequest.getUserId()));
        if (event.getParticipants().stream().anyMatch(userEntity -> userEntity.getId().equals(subscribeEventRequest.getUserId()))) {
            throw new UserAlreadySubscribedException(
                    subscribeEventRequest.getUserId(),
                    subscribeEventRequest.getEventId(),
                    UserAlreadySubscribedException.SubscriptionType.EVENT);
        }

        event.getParticipants().add(user);
        user.getSubscribedEvents().add(event);

        userRepository.save(user);
        return applicationMapper.map(eventRepository.save(event));
    }
}
