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
            var builder = ChatServiceProto.CreateChatRoomRequest.newBuilder()
                    .setChannelName(request.getName())
                    .setUuid(request.getUuid());

            if (request.getGroup() != null) {
                builder.setGroupName(request.getGroup());
            }

            var result = chatServiceStub.createChannel(builder.build());

            return CreateChatChannelResponse.builder()
                    .channelName(result.getChannelName())
                    .groupName(result.getGroupName())
                    .build();
        } catch (Exception e) {
            throw new ChatServiceException(e.getMessage());
        }
    }
}
