package app.groopy.wallservice.infrastructure.repository;

import app.groopy.wallservice.infrastructure.models.CreateChatChannelRequest;
import app.groopy.wallservice.infrastructure.models.CreateChatChannelResponse;

public interface ChatProviderRepository {

    CreateChatChannelResponse createChannel(CreateChatChannelRequest request);
}
