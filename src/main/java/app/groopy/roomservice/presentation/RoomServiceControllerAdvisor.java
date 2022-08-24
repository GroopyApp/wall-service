package app.groopy.roomservice.presentation;

import app.groopy.protobuf.RoomServiceProto;
import app.groopy.roomservice.domain.exceptions.CreateRoomValuesValidationException;
import app.groopy.roomservice.domain.exceptions.RoomNotFoundException;
import app.groopy.roomservice.domain.exceptions.UserNotFoundException;
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
                .setError(RoomServiceProto.Error.newBuilder()
                        .setDescription(ex.getLocalizedMessage()).build())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Object> handle(
            RoomNotFoundException ex, WebRequest request) {

        RoomServiceProto.ListRoomResponse response = RoomServiceProto.ListRoomResponse.newBuilder()
                .setError(RoomServiceProto.Error.newBuilder()
                        .setDescription(ex.getLocalizedMessage()).build())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handle(
            UserNotFoundException ex, WebRequest request) {

        RoomServiceProto.ListRoomResponse response = RoomServiceProto.ListRoomResponse.newBuilder()
                .setError(RoomServiceProto.Error.newBuilder()
                        .setDescription(ex.getLocalizedMessage()).build())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}