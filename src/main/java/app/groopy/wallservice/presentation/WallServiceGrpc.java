package app.groopy.wallservice.presentation;

import app.groopy.protobuf.WallServiceProto;
import app.groopy.wallservice.application.WallService;
import app.groopy.wallservice.presentation.mapper.PresentationMapper;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class WallServiceGrpc extends app.groopy.protobuf.WallServiceGrpc.WallServiceImplBase {

    private final Logger LOGGER = LoggerFactory.getLogger(WallServiceGrpc.class);

    private final WallService wallService;
    private final PresentationMapper presentationMapper;

    @Autowired
    public WallServiceGrpc(WallService wallService, PresentationMapper presentationMapper) {
        this.wallService = wallService;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public void getWall(WallServiceProto.GetWallRequest request, StreamObserver<WallServiceProto.GetWallResponse> responseObserver) {
        LOGGER.info("Processing GetWall request {}", request);
        WallServiceProto.GetWallResponse response = presentationMapper.map(
                wallService.get(presentationMapper.map(request.getCriteria()))
        );
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}