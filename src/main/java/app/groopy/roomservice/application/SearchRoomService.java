package app.groopy.roomservice.application;

import app.groopy.commons.application.StandardService;
import app.groopy.providers.elasticsearch.models.SearchScope;
import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.application.validators.RoomServiceValidator;
import app.groopy.roomservice.domain.exceptions.ListRoomException;
import app.groopy.roomservice.domain.exceptions.ListUserRoomException;
import app.groopy.roomservice.domain.models.ContextWrappedRequestDto;
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
public class SearchRoomService extends StandardService<ContextWrappedRequestDto<ListRoomRequestDto, SearchScope>, ListRoomResponseDto> {

    @Autowired
    private RoomServiceValidator validator;
    @Autowired
    private ApplicationMapper mapper;

    @Autowired
    private ElasticsearchInfrastructureService infrastructureService;

    @SneakyThrows
    public ListRoomResponseDto perform(ContextWrappedRequestDto<ListRoomRequestDto, SearchScope> requestDto) {
        validator.validate(requestDto.getRequestDto());
        try {
            List<RoomDetailsDto> result = infrastructureService.findBySearchRequest(requestDto.getRequestDto(), requestDto.getContext()).stream()
                    .map(entity -> mapper.map(entity)).collect(Collectors.toList());
            return ListRoomResponseDto.builder()
                    .rooms(result)
                    .build();
        } catch (ElasticsearchServiceException e) {
            LOGGER.error("an error occurred trying to search for rooms", e);
            throw new ListRoomException(requestDto.getRequestDto(), e.getLocalizedMessage());
        }
    }
}
