package app.groopy.wallservice.presentation.resolver;

import app.groopy.wallservice.domain.models.ErrorDto;
import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;

import java.util.HashMap;
import java.util.Map;

public class ErrorResolver {
    private static final String ENTITY = "entity";
    private static final String ALREADY_EXISTING_ID = "existingId";
    private static final String NOT_FOUND_ID = "notFoundId";

    public static StatusRuntimeException resolve(ErrorDto errorResponse) {
            return StatusProto.toStatusRuntimeException(com.google.rpc.Status.newBuilder()
            .setCode(resolveCode(errorResponse))
            .setMessage(errorResponse.getErrorDescription())
            .addDetails(Any.pack(ErrorInfo.newBuilder()
                            .putAllMetadata(resolveMetadata(errorResponse))
                    .build()))
            .build());
    }

    private static Map<String, String> resolveMetadata(ErrorDto errorResponse) {
        Map<String, String> result = new HashMap<>();
        if (errorResponse.getEntityName() != null) {
            result.put(ENTITY, errorResponse.getEntityName());
        }
        if (errorResponse.getExistingEntityId() != null) {
            result.put(ALREADY_EXISTING_ID, errorResponse.getExistingEntityId());
        }
        if (errorResponse.getNotFoundId() != null) {
            result.put(NOT_FOUND_ID, errorResponse.getNotFoundId());
        }
        return result;
    }

    private static int resolveCode(ErrorDto errorResponse) {
        if (errorResponse.getNotFoundId() != null) {
            return Code.NOT_FOUND.getNumber();
        }
        else if (errorResponse.getExistingEntityId() != null) {
            return Code.ALREADY_EXISTS.getNumber();
        }
        return Code.UNKNOWN.getNumber();
    }
}
