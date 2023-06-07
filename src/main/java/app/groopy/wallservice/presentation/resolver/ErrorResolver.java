package app.groopy.wallservice.presentation.resolver;

import app.groopy.wallservice.application.exception.ApplicationAlreadyExistsException;
import app.groopy.wallservice.application.exception.ApplicationBadRequestException;
import app.groopy.wallservice.application.exception.ApplicationException;
import app.groopy.wallservice.application.exception.ApplicationNotFoundException;
import app.groopy.wallservice.domain.models.ErrorMetadataDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ErrorResolver {

    public static StatusRuntimeException resolve(ApplicationException exception) {
            return StatusProto.toStatusRuntimeException(com.google.rpc.Status.newBuilder()
            .setCode(resolveCode(exception))
            .setMessage(exception.getErrorResponse().getErrorDescription())
            .addDetails(Any.pack(ErrorInfo.newBuilder()
                            .putAllMetadata(resolveMetadata(exception.getErrorResponse()))
                    .build()))
            .build());
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
