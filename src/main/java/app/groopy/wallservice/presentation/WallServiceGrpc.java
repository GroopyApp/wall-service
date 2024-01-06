package app.groopy.wallservice.presentation;

import app.groopy.protobuf.WallServiceProto;
import app.groopy.wallservice.application.ApplicationService;
import app.groopy.wallservice.application.exceptions.ApplicationException;
import app.groopy.wallservice.domain.models.SearchCriteriaDto;
import app.groopy.wallservice.presentation.mapper.PresentationMapper;
import app.groopy.wallservice.presentation.resolver.ApplicationExceptionResolver;
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
    public void getWall(WallServiceProto.WallRequest request, StreamObserver<WallServiceProto.WallResponse> responseObserver) {
        LOGGER.info("Processing WallRequest {}", request);
        try {
            SearchCriteriaDto searchCriteriaDto = presentationMapper.map(request.getCriteria());

            searchCriteriaDto = searchCriteriaDto.toBuilder()
                    .onlyFutureEvents(true)
                    .build();

            var userId = searchCriteriaDto.getUserId();

            var topics = userId != null && !userId.isBlank() ?
                    applicationService.getSubscribedUserTopics(searchCriteriaDto)
                    : applicationService.find(searchCriteriaDto);

            responseObserver.onNext(WallServiceProto.WallResponse.newBuilder().addAllTopics(topics.stream()
                            .map(presentationMapper::map)
                            .toList())
                    .build());
            responseObserver.onCompleted();
        } catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
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
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }

    @Override
    public void subscribeTopic(WallServiceProto.SubscribeTopicRequest request, StreamObserver<WallServiceProto.SubscribeTopicResponse> responseObserver) {
        LOGGER.info("Processing SubscribeTopicRequest {}", request);
        try {
            responseObserver.onNext(WallServiceProto.SubscribeTopicResponse.newBuilder()
                    .setTopic(presentationMapper.map(
                            applicationService.updateSubscription(
                                    presentationMapper.map(request))
                    )).build());
            responseObserver.onCompleted();
        }
        catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }

    @Override
    public void subscribeEvent(WallServiceProto.SubscribeEventRequest request, StreamObserver<WallServiceProto.SubscribeEventResponse> responseObserver) {
        LOGGER.info("Processing SubscribeEventRequest {}", request);
        try {
            responseObserver.onNext(WallServiceProto.SubscribeEventResponse.newBuilder()
                    .setEvent(presentationMapper.map(
                            applicationService.updateSubscription(
                                    presentationMapper.map(request))
                    )).build());
            responseObserver.onCompleted();
        }
        catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
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
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }

    @Override
    public void publishThread(WallServiceProto.PublishThreadRequest request, StreamObserver<WallServiceProto.PublishThreadResponse> responseObserver) {
        LOGGER.info("Processing PublishThreadRequest {}", request);
        try {
            applicationService.publishThread(presentationMapper.map(request));
            responseObserver.onNext(WallServiceProto.PublishThreadResponse.newBuilder()
                    .setStatus(200).build());
            responseObserver.onCompleted();
        }
        catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }
}
