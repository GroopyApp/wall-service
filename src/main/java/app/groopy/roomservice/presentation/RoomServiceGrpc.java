package app.groopy.roomservice.presentation;

import app.groopy.protobuf.RoomServiceProto;
import app.groopy.providers.elasticsearch.models.SearchScope;
import app.groopy.roomservice.application.CreateRoomService;
import app.groopy.roomservice.application.ListRoomService;
import app.groopy.roomservice.application.SearchRoomService;
import app.groopy.roomservice.application.SubscribeService;
import app.groopy.roomservice.domain.models.ContextWrappedRequestDto;
import app.groopy.roomservice.domain.models.ListRoomRequestDto;
import app.groopy.roomservice.presentation.mapper.PresentationMapper;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class RoomServiceGrpc extends app.groopy.protobuf.RoomServiceGrpc.RoomServiceImplBase {

    private final Logger LOGGER = LoggerFactory.getLogger(RoomServiceGrpc.class);
    @Autowired
    private CreateRoomService createRoomService;
    @Autowired
    private ListRoomService listRoomService;
    @Autowired
    private SearchRoomService searchRoomService;
    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private PresentationMapper presentationMapper;

    @Override
    public void createRoom(RoomServiceProto.CreateRoomRequest request, StreamObserver<RoomServiceProto.CreateRoomResponse> responseObserver) {
        LOGGER.info("Processing message {}", request);
        RoomServiceProto.CreateRoomResponse response = presentationMapper.map(
                createRoomService.perform(presentationMapper.map(request))
        );

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchRoom(RoomServiceProto.ListRoomRequest request, StreamObserver<RoomServiceProto.ListRoomResponse> responseObserver) {
        LOGGER.info("Processing message {}", request);
        RoomServiceProto.ListRoomResponse response = presentationMapper.map(
                searchRoomService.perform(ContextWrappedRequestDto.<ListRoomRequestDto, SearchScope>builder()
                                .requestDto(presentationMapper.map(request))
                        .context(SearchScope.STANDARD_SEARCH)
                        .build())
        );
        LOGGER.info("Sending ListRoomResponse {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listRoom(RoomServiceProto.ListRoomRequest request, StreamObserver<RoomServiceProto.ListRoomResponse> responseObserver) {
        LOGGER.info("Processing message {}", request);
        RoomServiceProto.ListRoomResponse response = presentationMapper.map(
                listRoomService.perform(request.getUserId())
        );
        LOGGER.info("Sending ListRoomResponse {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void subscribe(RoomServiceProto.SubscribeRoomRequest request, StreamObserver<RoomServiceProto.SubscribeRoomResponse> responseObserver) {
        LOGGER.info("Processing message {}", request);
        subscribeService.subscribe(request.getUserId(), request.getRoomId());
        RoomServiceProto.SubscribeRoomResponse response = presentationMapper.map(subscribeService.subscribe(request.getUserId(), request.getRoomId()));
        LOGGER.info("Sending SubscribeRoomResponse {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}