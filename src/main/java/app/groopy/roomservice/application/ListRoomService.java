package app.groopy.roomservice.application;

import app.groopy.commons.infrastructure.repository.models.SearchScope;
import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.domain.models.ListRoomRequestDto;
import app.groopy.roomservice.domain.models.ListRoomResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import app.groopy.roomservice.application.validators.ListRoomValidator;
import app.groopy.roomservice.infrastructure.ElasticsearchInfrastructureService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListRoomService extends app.groopy.commons.application.Service<String, ListRoomResponseDto>{

    private final Logger LOGGER = LoggerFactory.getLogger(ListRoomService.class);

    @Autowired
    private ListRoomValidator validator;
    @Autowired
    private ApplicationMapper mapper;

    @Autowired
    private ElasticsearchInfrastructureService infrastructureService;

    public ListRoomResponseDto searchRoom(ListRoomRequestDto request, SearchScope searchScope) {
        validator.validate(request);

        List<RoomDetailsDto> result = infrastructureService.findBySearchRequest(request, searchScope).stream()
                .map(entity -> mapper.map(entity)).collect(Collectors.toList());
        return ListRoomResponseDto.builder()
                .rooms(result)
                .build();
    }

    @SneakyThrows
    public ListRoomResponseDto perform(String userId) {
        validator.validate(userId);

        List<RoomDetailsDto> result = infrastructureService.findByUserId(userId).stream().map(entity -> mapper.map(entity)).collect(Collectors.toList());
        return ListRoomResponseDto.builder()
                .rooms(result)
                .build();
    }
}
