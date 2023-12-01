package app.groopy.wallservice.infrastructure.providers;

import app.groopy.protobuf.ChatServiceGrpc;
import app.groopy.protobuf.ChatServiceProto;
import app.groopy.wallservice.infrastructure.models.CreateChatChannelRequest;
import app.groopy.wallservice.infrastructure.models.CreateChatChannelResponse;
import app.groopy.wallservice.infrastructure.providers.exceptions.ChatServiceException;
import app.groopy.wallservice.infrastructure.repository.ChatProviderRepository;
import lombok.SneakyThrows;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceClient implements ChatProviderRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(ChatServiceClient.class);

    @GrpcClient("chatService")
    ChatServiceGrpc.ChatServiceBlockingStub chatServiceStub;

    @SneakyThrows
    public CreateChatChannelResponse createChannel(CreateChatChannelRequest request) {
        try {

            var result = chatServiceStub.createChannel(ChatServiceProto.CreateChatRoomRequest.newBuilder()
                    .setChannelName(request.getName())
                    .setGroupName(request.getGroup().name())
                    .setUuid(request.getUuid())
                    .build());

            return CreateChatChannelResponse.builder()
                    .channelName(result.getChannelName())
                    .groupName(result.getGroupName())
                    .build();
        } catch (Exception e) {
            throw new ChatServiceException(e.getMessage());
        }
    }
}
