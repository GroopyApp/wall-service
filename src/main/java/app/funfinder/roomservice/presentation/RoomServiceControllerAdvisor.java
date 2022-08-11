package app.funfinder.roomservice.presentation;

import app.funfinder.protobuf.RoomServiceProto;
import app.funfinder.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RoomServiceControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CreateRoomValuesValidationException.class)
    public ResponseEntity<Object> handle(
            CreateRoomValuesValidationException ex, WebRequest request) {

        RoomServiceProto.CreateRoomResponse response = RoomServiceProto.CreateRoomResponse.newBuilder()
                .setError(RoomServiceProto.CreateRoomError.newBuilder()
                        .setDescription(ex.getLocalizedMessage()).build())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}