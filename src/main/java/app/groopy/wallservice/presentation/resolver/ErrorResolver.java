package app.groopy.wallservice.presentation.resolver;

import app.groopy.protobuf.WallServiceProto;
import app.groopy.wallservice.application.exceptions.ApplicationAlreadyExistsException;
import app.groopy.wallservice.application.exceptions.ApplicationBadRequestException;
import app.groopy.wallservice.application.exceptions.ApplicationException;
import app.groopy.wallservice.application.exceptions.ApplicationNotFoundException;
import app.groopy.wallservice.domain.models.ErrorMetadataDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.rpc.Code;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.protobuf.StatusProto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ErrorResolver {

    public static StatusRuntimeException resolve(ApplicationException exception) {
        var errorResponseKey = ProtoUtils.keyForProto(WallServiceProto.ErrorResponse.getDefaultInstance());
        Metadata metadata = new Metadata();

        var errorResponse = WallServiceProto.ErrorResponse.newBuilder()
                .putAllParameters(resolveMetadata(exception.getErrorResponse())).build();
        metadata.put(errorResponseKey, errorResponse);

            return StatusProto.toStatusRuntimeException(com.google.rpc.Status.newBuilder()
            .setCode(resolveCode(exception))
            .setMessage(exception.getErrorResponse().getErrorDescription())
            .build(), metadata);
    }

    private static Map<String, String> resolveMetadata(ErrorMetadataDto errorMetadataDto) {
        return new ObjectMapper().convertValue(errorMetadataDto, new TypeReference<HashMap<String, String>>() {})
                .entrySet().stream().filter(entry -> entry.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static int resolveCode(ApplicationException exception) {

        if (exception instanceof ApplicationNotFoundException) {
            return Code.NOT_FOUND.getNumber();
        }
        else if (exception instanceof ApplicationAlreadyExistsException) {
            return Code.ALREADY_EXISTS.getNumber();
        }
        else if (exception instanceof ApplicationBadRequestException) {
            return Code.INVALID_ARGUMENT.getNumber();
        }
        return Code.UNKNOWN.getNumber();
    }
}
