package app.groopy.roomservice.application;

import app.groopy.roomservice.application.mapper.ApplicationMapper;
import app.groopy.roomservice.domain.exceptions.CreateRoomException;
import app.groopy.roomservice.domain.models.CreateRoomRequestDto;
import app.groopy.roomservice.domain.models.CreateRoomResponseDto;
import app.groopy.roomservice.domain.models.common.RoomDetailsDto;
import app.groopy.roomservice.application.validators.RoomServiceValidator;
import app.groopy.roomservice.infrastructure.ElasticsearchInfrastructureService;
import app.groopy.roomservice.infrastructure.exceptions.ElasticsearchServiceException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateRoomService extends app.groopy.commons.application.StandardService<CreateRoomRequestDto, CreateRoomResponseDto> {

    @Autowired
    private RoomServiceValidator validator;
    @Autowired
    private ApplicationMapper mapper;
    @Autowired
    private ElasticsearchInfrastructureService infrastructureService;

   @SneakyThrows
    public CreateRoomResponseDto perform(CreateRoomRequestDto request) {
        validator.validate(request);

        final String roomId = UUID.nameUUIDFromBytes(assembleId(request)).toString();

       try {
           RoomDetailsDto result = mapper.map(infrastructureService.storeRoom(roomId, request));

           infrastructureService.subscribeUserToRoom(request.getCreator(), result.getRoomId());

           LOGGER.info("Room correctly stored in ES");

           return CreateRoomResponseDto.builder()
                   .room(result)
                   .build();
       } catch (ElasticsearchServiceException e) {
           LOGGER.error("an error occurred trying to create a room", e);
           throw new CreateRoomException(request, e.getLocalizedMessage());
       }
   }

    private byte[] assembleId(CreateRoomRequestDto request) {
        StringBuilder sb = new StringBuilder(request.getRoomName());
        request.getHashtags().forEach(sb::append);
        request.getLanguages().forEach(sb::append);
        sb.append(
                request.getRoomLocation().getLatitude())
                .append(
                 request.getRoomLocation().getLongitude()
                );
        sb.append(LocalDateTime.now());
        return sb.toString().getBytes();
    }
}
