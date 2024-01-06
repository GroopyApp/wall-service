package app.groopy.wallservice.domain.services;

import app.groopy.wallservice.application.mapper.ApplicationMapper;
import app.groopy.wallservice.domain.exceptions.EventNotFoundException;
import app.groopy.wallservice.domain.exceptions.TopicNotFoundException;
import app.groopy.wallservice.domain.exceptions.UserNotFoundException;
import app.groopy.wallservice.domain.exceptions.WallNotFoundException;
import app.groopy.wallservice.domain.models.GroupType;
import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.domain.models.entities.EventDto;
import app.groopy.wallservice.domain.models.entities.TopicDto;
import app.groopy.wallservice.domain.models.requests.*;
import app.groopy.wallservice.domain.utils.Utils;
import app.groopy.wallservice.infrastructure.models.*;
import app.groopy.wallservice.infrastructure.repository.*;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainService.class);

    private final TopicRepository topicRepository;
    private final EventRepository eventRepository;
    private final WallRepository wallRepository;
    private final UserRepository userRepository;
    private final ChatProviderRepository chatProviderRepository;
    private final MongoTemplate mongoTemplate;

    private final Validator validator;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public DomainService(
            TopicRepository topicRepository,
            EventRepository eventRepository,
            WallRepository wallRepository,
            UserRepository userRepository,
            ChatProviderRepository chatProviderRepository,
            MongoTemplate mongoTemplate,
            Validator validator,
            ApplicationMapper applicationMapper) {
        this.topicRepository = topicRepository;
        this.eventRepository = eventRepository;
        this.wallRepository = wallRepository;
        this.userRepository = userRepository;
        this.chatProviderRepository = chatProviderRepository;
        this.mongoTemplate = mongoTemplate;
        this.validator = validator;
        this.applicationMapper = applicationMapper;
    }

    @SneakyThrows
    public List<TopicDto> getTopicsBy(SearchCriteriaDto requestCriteria) {
        LOGGER.info("fetching topics by requestCriteria: {}", requestCriteria);
        validator.validateSearch(requestCriteria);
        WallEntity wall = wallRepository.findByLocationId(requestCriteria.getLocation().getLocationId())
                .orElseThrow(() -> {
                    LOGGER.error("No wall found for specific location: {}", requestCriteria.getLocation());
                    return new WallNotFoundException(
                            requestCriteria.getLocation().getLocationId(),
                            WallNotFoundException.WallSearchType.LOCATION_ID);
                });
        return topicRepository.findByCriteria(
                mongoTemplate,
                wall.getId(),
                requestCriteria.getHashtags(),
                requestCriteria.getLanguages(),
                requestCriteria.isOnlyFutureEvents()).stream().map(applicationMapper::map).toList();
    }

    @SneakyThrows
    public List<TopicDto> getUserTopics(SearchCriteriaDto requestCriteria) {
        LOGGER.info("fetching user topics by requestCriteria: {}", requestCriteria);
        validator.validateUserSearch(requestCriteria);
        //fetch user
        UserEntity user = userRepository.findByUserId(requestCriteria.getUserId())
                .orElseThrow(() -> {
                    LOGGER.error("No user found for specific id: {}", requestCriteria.getUserId());
                    return new UserNotFoundException(requestCriteria.getUserId());
                });
        //fetch wall by location (a user can only see topics from a specific wall defined by his actual location) should we remove this restriction?
        WallEntity wall = wallRepository.findByLocationId(requestCriteria.getLocation().getLocationId())
                .orElseThrow(() -> {
                    LOGGER.error("No wall found for specific location: {}", requestCriteria.getLocation());
                    return new WallNotFoundException(
                            requestCriteria.getLocation().getLocationId(),
                            WallNotFoundException.WallSearchType.LOCATION_ID);
                });
        // get all topics subscribed by user, filter by wall and search criteria and including only future events
        List<TopicEntity> userTopics = user.getSubscribedTopics().stream().filter(topic ->
                topic.getWall().getId().equals(wall.getId()) && filterBySearchCriteria(topic, requestCriteria))
                .peek(topic -> topic.setEvents(topic.getEvents().stream()
                    .filter(eventEntity -> eventEntity.getStartDate().isAfter(LocalDateTime.now()))
                    .toList()))
                .toList();

        return userTopics.stream()
                .map(applicationMapper::map)
                .toList();
    }

    @SneakyThrows
    public TopicDto createTopic(CreateTopicRequestDto createTopicRequest) {
        LOGGER.info("creating new topic with request parameters: {}", createTopicRequest);
        var identifier = Utils.generateUUID(createTopicRequest);
        LOGGER.info("new UUID generated for topic creation: {}", identifier);
        validator.validate(identifier, TopicEntity.class);
        WallEntity wall = wallRepository.findById(createTopicRequest.getWallId())
                .orElseThrow(() -> {
                    LOGGER.error("No wall found for specific id: {}", createTopicRequest.getWallId());
                    return new WallNotFoundException(createTopicRequest.getWallId(), WallNotFoundException.WallSearchType.ID);
                });

        CreateChatChannelResponse chatResponse = chatProviderRepository.createChannel(CreateChatChannelRequest.builder()
                        .name(createTopicRequest.getName())
                        .group(GroupType.TOPIC)
                        .uuid(identifier)
                .build());

        TopicEntity topic = topicRepository.save(TopicEntity.builder()
                .publisher(createTopicRequest.getUserId())
                .wall(wall)
                .name(createTopicRequest.getName())
                .description(createTopicRequest.getDescription())
                .imageUrl(createTopicRequest.getImageUrl())
                .categories(createTopicRequest.getCategories())
                .language(createTopicRequest.getLanguage())
                .events(new ArrayList<>())
                .chatInfo(ChatInfo.builder()
                        .chatName(chatResponse.getChannelName())
                        .groupName(chatResponse.getGroupName())
                        .uuid(identifier)
                        .build())
                .threads(new ArrayList<>())
                .build());
        wall.getTopics().add(topic);
        wallRepository.save(wall);
        return applicationMapper.map(topic);
    }

    @SneakyThrows
    public TopicDto createEvent(CreateEventRequestDto createEventRequest) {
        LOGGER.info("creating new topic with request parameters: {}", createEventRequest);
        validator.validate(createEventRequest);

        TopicEntity topic = topicRepository.findById(createEventRequest.getTopicId())
                .orElseThrow(() -> {
                    LOGGER.error("No topic found with specific id: {}", createEventRequest.getTopicId());
                    return new TopicNotFoundException(createEventRequest.getTopicId());
                });

        var identifier = Utils.generateUUID(createEventRequest);
        LOGGER.info("new UUID generated for event creation: {}", identifier);

        validator.validate(identifier, topic.getEvents());

        CreateChatChannelResponse chatResponse = chatProviderRepository.createChannel(CreateChatChannelRequest.builder()
                .name(topic.getName())
                .group(GroupType.EVENT)
                .uuid(identifier)
                .build());

        EventEntity event = eventRepository.save(EventEntity.builder()
                        .publisher(createEventRequest.getUserId())
                        .topic(topic)
                        .identifier(identifier)
                        .chatInfo(ChatInfo.builder()
                            .chatName(chatResponse.getChannelName())
                            .groupName(chatResponse.getGroupName())
                            .uuid(identifier)
                            .build())
                        .eventLocationId(createEventRequest.getLocationId())
                        .imageUrl(createEventRequest.getImageUrl())
                        .description(createEventRequest.getDescription())
                        .name(createEventRequest.getName())
                        .startDate(createEventRequest.getStartDate())
                        .endDate(createEventRequest.getEndDate())
                        .participants(new ArrayList<>())
                        .threads(new ArrayList<>())
                .build());
        topic.getEvents().add(event);
        topic = topicRepository.save(topic);
        return applicationMapper.map(topic);
    }

    @SneakyThrows
    public TopicDto updateTopicSubscription(SubscribeTopicRequestDto subscribeTopicRequest) {
        LOGGER.info("processing new topic subscription: {}", subscribeTopicRequest);
        TopicEntity topic = topicRepository.findById(subscribeTopicRequest.getTopicId())
                .orElseThrow(() -> {
                    LOGGER.error("No topic found with specific id: {}", subscribeTopicRequest.getTopicId());
                    return new TopicNotFoundException(subscribeTopicRequest.getTopicId());
                });
        UserEntity user = userRepository.findByUserId(subscribeTopicRequest.getUserId())
                .orElseThrow(() -> {
                    LOGGER.error("No user found with specific id: {}", subscribeTopicRequest.getUserId());
                    return new UserNotFoundException(subscribeTopicRequest.getUserId());
                });
        if (topic.getSubscribers().stream().anyMatch(userEntity ->
                userEntity.getUserId().equals(subscribeTopicRequest.getUserId()))) {
            topic.setSubscribers(topic.getSubscribers().stream().filter(elem ->
                    !elem.getUserId().equals(subscribeTopicRequest.getUserId())).toList());

            user.setSubscribedTopics(
                    user.getSubscribedTopics().stream()
                            .filter(elem -> !elem.getId().equals(subscribeTopicRequest.getTopicId()))
                            .toList());
        } else {
            topic.getSubscribers().add(user);
            user.getSubscribedTopics().add(topic);
        }
        userRepository.save(user);
        return applicationMapper.map(topicRepository.save(topic));
    }

    @SneakyThrows
    public EventDto updateEventSubscription(SubscribeEventRequestDto subscribeEventRequest) {
        LOGGER.info("processing new event subscription: {}", subscribeEventRequest);
        EventEntity event = eventRepository.findById(subscribeEventRequest.getEventId())
                .orElseThrow(() -> {
                    LOGGER.error("No event found with specific id: {}", subscribeEventRequest.getEventId());
                    return new EventNotFoundException(subscribeEventRequest.getEventId());
                });
        UserEntity user = userRepository.findByUserId(subscribeEventRequest.getUserId())
                .orElseThrow(() -> {
                    LOGGER.error("No user found with specific id: {}", subscribeEventRequest.getUserId());
                    return new UserNotFoundException(subscribeEventRequest.getUserId());
                });
        if (event.getParticipants().stream().anyMatch(userEntity ->
                userEntity.getUserId().equals(subscribeEventRequest.getUserId()))) {
            event.setParticipants(event.getParticipants().stream().filter(elem ->
                    !elem.getUserId().equals(subscribeEventRequest.getUserId())).toList());
            user.setSubscribedEvents(
                    user.getSubscribedEvents().stream()
                            .filter(elem -> !elem.getId().equals(subscribeEventRequest.getEventId()))
                            .toList());
        } else {
            event.getParticipants().add(user);
            user.getSubscribedEvents().add(event);
        }

        userRepository.save(user);
        return applicationMapper.map(eventRepository.save(event));
    }

    @SneakyThrows
    public void publishThread(PublishThreadRequestDto request) {
        LOGGER.info("processing new thread publishing: {}", request);
        ThreadEntity thread = ThreadEntity.builder()
                .id(request.getThreadId())
                .build();
        TopicEntity topicEntity = topicRepository.findById(request.getTopicId()).orElseThrow(() -> {
            LOGGER.error("No topic found with specific id: {}", request.getTopicId());
            return new TopicNotFoundException(request.getTopicId());
        });
        if (StringUtils.hasText(request.getEventId())) {
            EventEntity eventEntity = topicEntity.getEvents().stream().filter(event -> event.getId().equals(request.getEventId()))
                    .findAny().orElseThrow(() -> {
                LOGGER.error("No event found with specific id: {}", request.getEventId());
                return new EventNotFoundException(request.getEventId());
            });
            eventEntity.getThreads().add(thread);
            eventRepository.save(eventEntity);
        } else {
            topicEntity.getThreads().add(thread);
            topicRepository.save(topicEntity);
        }
    }

    private boolean filterBySearchCriteria(TopicEntity topic, SearchCriteriaDto requestCriteria) {
        return requestCriteria.getHashtags() != null && !requestCriteria.getHashtags().isEmpty() ?
                new HashSet<>(topic.getCategories()).containsAll(requestCriteria.getHashtags())
                : requestCriteria.getLanguages() == null
                || requestCriteria.getLanguages().isEmpty()
                || requestCriteria.getLanguages().contains(topic.getLanguage());
    }
}
