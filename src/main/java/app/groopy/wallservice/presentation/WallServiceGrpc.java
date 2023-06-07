package app.groopy.wallservice.presentation;

import app.groopy.protobuf.WallServiceProto;
import app.groopy.wallservice.application.ApplicationService;
import app.groopy.wallservice.application.exception.ApplicationException;
import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.presentation.mapper.PresentationMapper;
import app.groopy.wallservice.presentation.resolver.ErrorResolver;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class WallServiceGrpc extends app.groopy.protobuf.WallServiceGrpc.WallServiceImplBase {

    private final Logger LOGGER = LoggerFactory.getLogger(WallServiceGrpc.class);

    private final ApplicationService applicationService;
    private final PresentationMapper presentationMapper;

    @Autowired
    public WallServiceGrpc(ApplicationService applicationService, PresentationMapper presentationMapper) {
        this.applicationService = applicationService;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public void getWall(WallServiceProto.GetWallRequest request, StreamObserver<WallServiceProto.GetWallResponse> responseObserver) {
        LOGGER.info("Processing GetWallRequest {}", request);
        try {
            SearchCriteriaDto searchCriteriaDto = presentationMapper.map(request.getCriteria());
            responseObserver.onNext(WallServiceProto.GetWallResponse.newBuilder().addAllTopics(
                            applicationService.get(searchCriteriaDto.toBuilder()
                                            .onlyValidEvents(true)
                                            .build()).stream()
                                    .map(presentationMapper::map)
                                    .toList())
                    .build());
            responseObserver.onCompleted();
        } catch (ApplicationException e) {
            responseObserver.onError(ErrorResolver.resolve(e));
        }
    }

    @Override
    public void createTopic(WallServiceProto.CreateTopicRequest request, StreamObserver<WallServiceProto.CreateTopicResponse> responseObserver) {
        LOGGER.info("Processing CreateTopicRequest {}", request);
        try {
            responseObserver.onNext(WallServiceProto.CreateTopicResponse.newBuilder()
                    .setTopic(presentationMapper.map(
                            applicationService.create(
                            presentationMapper.map(request))
                    )).build());
            responseObserver.onCompleted();
        }
        catch (ApplicationException e) {
            responseObserver.onError(ErrorResolver.resolve(e));
        }
    }

    @Override
    public void createEvent(WallServiceProto.CreateEventRequest request, StreamObserver<WallServiceProto.CreateEventResponse> responseObserver) {
        LOGGER.info("Processing CreateEventRequest {}", request);
        try {
            responseObserver.onNext(WallServiceProto.CreateEventResponse.newBuilder()
                    .setTopic(presentationMapper.map(
                            applicationService.create(
                                    presentationMapper.map(request))
                    )).build());
            responseObserver.onCompleted();
        }
        catch (ApplicationException e) {
            responseObserver.onError(ErrorResolver.resolve(e));
        }
    }
}