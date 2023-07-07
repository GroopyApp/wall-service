package app.groopy.wallservice.application;

import app.groopy.wallservice.application.exceptions.ApplicationException;
import app.groopy.wallservice.domain.models.*;
import app.groopy.wallservice.domain.models.entities.EventDto;
import app.groopy.wallservice.domain.models.entities.TopicDto;
import app.groopy.wallservice.domain.models.requests.CreateEventRequestDto;
import app.groopy.wallservice.domain.models.requests.CreateTopicRequestDto;
import app.groopy.wallservice.domain.models.requests.SubscribeEventRequestDto;
import app.groopy.wallservice.domain.models.requests.SubscribeTopicRequestDto;
import app.groopy.wallservice.domain.resolver.InfrastructureExceptionResolver;
import app.groopy.wallservice.domain.services.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private final DomainService domainService;

    @Autowired
    public ApplicationService(DomainService domainService) {
        this.domainService = domainService;
    }

    public List<TopicDto> find(SearchCriteriaDto criteria) throws ApplicationException {
        try {
            var result = domainService.getTopicsBy(criteria);
            LOGGER.info("returning results for get topics request: {}", result);
            return result;
        } catch (Exception e) {
            throw InfrastructureExceptionResolver.resolve(e);
        }
    }

    public List<TopicDto> getSubscribedUserTopics(SearchCriteriaDto criteria) throws ApplicationException {
        try {
            var result = domainService.getUserTopics(criteria);
            LOGGER.info("returning results for get topics request: {}", result);
            return result;
        } catch (Exception e) {
            throw InfrastructureExceptionResolver.resolve(e);
        }
    }

    public TopicDto create(CreateTopicRequestDto createTopicRequest) throws ApplicationException {
        try {
            var result = domainService.createTopic(createTopicRequest);
            LOGGER.info("returning resulting topic after creation: {}", result);
            return result;
        } catch (Exception e) {
            throw InfrastructureExceptionResolver.resolve(e);
        }
    }

    public TopicDto create(CreateEventRequestDto createEventRequest) throws ApplicationException {
        try {
            var result = domainService.createEvent(createEventRequest);
            LOGGER.info("returning resulting event after creation: {}", result);
            return result;
        } catch (Exception e) {
            throw InfrastructureExceptionResolver.resolve(e);
        }
    }

    public TopicDto subscribe(SubscribeTopicRequestDto subscribeTopicRequest) throws ApplicationException {
        try {
            var result = domainService.subscribeTopic(subscribeTopicRequest);
            LOGGER.info("returning resulting topic after subscription: {}", result);
            return result;
        } catch (Exception e) {
            throw InfrastructureExceptionResolver.resolve(e);
        }
    }

    public EventDto subscribe(SubscribeEventRequestDto subscribeEventRequest) throws ApplicationException {
        try {
            var result = domainService.subscribeEvent(subscribeEventRequest);
            LOGGER.info("returning resulting event after subscription: {}", result);
            return result;
        } catch (Exception e) {
            throw InfrastructureExceptionResolver.resolve(e);
        }
    }
}
