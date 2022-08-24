package app.groopy.roomservice.config;

import app.groopy.protobuf.RoomServiceProto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    ProtobufJsonFormatHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufJsonFormatHttpMessageConverter();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(protobufHttpMessageConverter());
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        JsonFormat.TypeRegistry typeRegistry = JsonFormat.TypeRegistry.newBuilder()
                .add(RoomServiceProto.CreateRoomRequest.getDescriptor())
                .add(RoomServiceProto.CreateRoomResponse.getDescriptor())
                .add(RoomServiceProto.ListRoomRequest.getDescriptor())
                .add(RoomServiceProto.ListRoomResponse.getDescriptor())
                .add(RoomServiceProto.SubscribeRoomRequest.getDescriptor())
                .add(RoomServiceProto.SubscribeRoomResponse.getDescriptor())
                .build();

        JsonFormat.Printer printer = JsonFormat.printer().usingTypeRegistry(typeRegistry)
                .includingDefaultValueFields();
        return o -> o.serializerByType(Message.class, new JsonSerializer<Message>() {
            @Override
            public void serialize(Message message, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeRawValue(printer.print(message));
            }
        });
    }
}
