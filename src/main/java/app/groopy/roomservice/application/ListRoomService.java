package app.groopy.roomservice.application;

import app.groopy.commons.application.StandardService;
import app.groopy.providers.elasticsearch.models.SearchScope;
import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.application.validators.RoomServiceValidator;
import app.groopy.roomservice.domain.exceptions.ListRoomException;
import app.groopy.roomservice.domain.exceptions.ListUserRoomException;
import app.groopy.roomservice.domain.models.ListRoomRequestDto;
import app.groopy.roomservice.domain.models.ListRoomResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import app.groopy.roomservice.infrastructure.ElasticsearchInfrastructureService;
import app.groopy.roomservice.infrastructure.exceptions.ElasticsearchServiceException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListRoomService extends StandardService<String, ListRoomResponseDto> {

    @Autowired
    private RoomServiceValidator validator;
    @Autowired
    private ApplicationMapper mapper;

    @Autowired
    private ElasticsearchInfrastructureService infrastructureService;

    @SneakyThrows
    public ListRoomResponseDto searchRoom(ListRoomRequestDto request, SearchScope searchScope) {
        validator.validate(request);
        try {
            List<RoomDetailsDto> result = infrastructureService.findBySearchRequest(request, searchScope).stream()
                    .map(entity -> mapper.map(entity)).collect(Collectors.toList());
            return ListRoomResponseDto.builder()
                    .rooms(result)
                    .build();
        } catch (ElasticsearchServiceException e) {
            LOGGER.error("an error occurred trying to search for rooms", e);
            throw new ListRoomException(request, e.getLocalizedMessage());
        }
    }

    @SneakyThrows
    public ListRoomResponseDto perform(String userId) {
        validator.validate(userId);
        try {
            List<RoomDetailsDto> result = infrastructureService.findByUserId(userId).stream().map(entity -> mapper.map(entity)).collect(Collectors.toList());
            return ListRoomResponseDto.builder()
                    .rooms(result)
                    .build();
        } catch (ElasticsearchServiceException e) {
            LOGGER.error("an error occurred trying to search for rooms for a user", e);
            throw new ListUserRoomException(userId, e.getLocalizedMessage());
        }
    }
}
