package app.groopy.wallservice.application;

import app.groopy.wallservice.application.exception.ApplicationException;
import app.groopy.wallservice.domain.exceptions.*;
import app.groopy.wallservice.domain.models.*;
import app.groopy.wallservice.domain.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final CrudService crudService;

    @Autowired
    public ApplicationService(CrudService crudService) {
        this.crudService = crudService;
    }

    public List<TopicDto> get(SearchCriteriaDto criteria) throws ApplicationException {
        try {
            return crudService.getTopicsBy(criteria);
        } catch (Exception e) {
            throw ApplicationExceptionResolver.resolve(e);
        }
    }

    public TopicDto create(CreateTopicRequestDto createTopicRequest) throws ApplicationException {
        try {
            return crudService.createTopic(createTopicRequest);
        } catch (Exception e) {
            throw ApplicationExceptionResolver.resolve(e);
        }
    }

    public TopicDto create(CreateEventRequestDto createEventRequest) throws ApplicationException {
        try {
            return crudService.createEvent(createEventRequest);
        } catch (Exception e) {
            throw ApplicationExceptionResolver.resolve(e);
        }
    }

    public TopicDto subscribe(SubscribeTopicRequestDto subscribeTopicRequest) throws ApplicationException {
        try {
            return crudService.subscribeTopic(subscribeTopicRequest);
        } catch (Exception e) {
            throw ApplicationExceptionResolver.resolve(e);
        }
    }

    public EventDto subscribe(SubscribeEventRequestDto subscribeEventRequest) throws ApplicationException {
        try {
            return crudService.subscribeEvent(subscribeEventRequest);
        } catch (Exception e) {
            throw ApplicationExceptionResolver.resolve(e);
        }
    }
}
