package app.groopy.wallservice.application;

import app.groopy.wallservice.application.exception.ApplicationAlreadyExistsException;
import app.groopy.wallservice.application.exception.ApplicationBadRequestException;
import app.groopy.wallservice.application.exception.ApplicationException;
import app.groopy.wallservice.application.exception.ApplicationNotFoundException;
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
}
