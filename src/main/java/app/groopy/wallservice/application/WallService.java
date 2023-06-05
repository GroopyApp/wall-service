package app.groopy.wallservice.application;

import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.domain.models.TopicDto;
import app.groopy.wallservice.domain.models.WallResponseDto;
import app.groopy.wallservice.domain.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WallService {

    private final CrudService crudService;

    @Autowired
    public WallService(CrudService crudService) {
        this.crudService = crudService;
    }

    public WallResponseDto get(SearchCriteriaDto criteria) {
        List<TopicDto> topics = crudService.getTopicsBy(criteria);
        return WallResponseDto.builder()
                .topics(topics)
                .build();
    }
}
