package app.groopy.wallservice.application;

import app.groopy.wallservice.application.exception.ApplicationException;
import app.groopy.wallservice.domain.exceptions.EntityAlreadyExistsException;
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

    public List<TopicDto> get(SearchCriteriaDto criteria) {
        return crudService.getTopicsBy(criteria);
    }

    public TopicDto create(CreateTopicRequestDto createTopicRequest) throws ApplicationException {
        try {
            return crudService.createTopic(createTopicRequest);
        } catch (EntityAlreadyExistsException e) {
            throw new ApplicationException(ErrorDto.builder()
                    .errorDescription(e.getLocalizedMessage())
                    .existingEntityId(e.getId())
                    .entityName(e.getEntityName())
                    .build());
        }
    }
}
