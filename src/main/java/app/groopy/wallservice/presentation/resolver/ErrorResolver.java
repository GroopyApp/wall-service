package app.groopy.wallservice.presentation.resolver;

import app.groopy.wallservice.domain.models.ErrorDto;
import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;

import java.util.HashMap;
import java.util.Map;

public class ErrorResolver {
    private static final String ALREADY_EXISTING_ID = "existingId";

    public static Status resolve(ErrorDto errorResponse) {
            return com.google.rpc.Status.newBuilder()
            .setCode(resolveCode(errorResponse))
            .setMessage(errorResponse.getErrorDescription())
            .addDetails(Any.pack(ErrorInfo.newBuilder()
                            .putAllMetadata(resolveMetadata(errorResponse))
                    .build()))
            .build();
    }

    private static Map<String, String> resolveMetadata(ErrorDto errorResponse) {
        Map<String, String> result = new HashMap<>();
        if (errorResponse.getExistingEntityId() != null) {
            result.put(ALREADY_EXISTING_ID, errorResponse.getExistingEntityId());
        }
        return result;
    }

    private static int resolveCode(ErrorDto errorResponse) {
        if (errorResponse.getExistingEntityId() != null) {
            return Code.ALREADY_EXISTS.getNumber();
        }
        return Code.UNKNOWN.getNumber();
    }
}
