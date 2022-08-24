package app.groopy.roomservice.presentation;

import app.groopy.protobuf.RoomServiceProto;
import app.groopy.roomservice.domain.exceptions.*;
import app.groopy.roomservice.presentation.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class RoomServiceControllerAdvisor extends ResponseEntityExceptionHandler {

    @Autowired
    private PresentationMapper mapper;

    @ExceptionHandler(CreateRoomValuesValidationException.class)
    public ResponseEntity<RoomServiceProto.ErrorResponse> handle(
            CreateRoomValuesValidationException ex, WebRequest request) {

        RoomServiceProto.ErrorResponse response = RoomServiceProto.ErrorResponse.newBuilder()
                .setDescription(ex.getLocalizedMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<RoomServiceProto.ErrorResponse> handle(
            RoomNotFoundException ex, WebRequest request) {

        RoomServiceProto.ErrorResponse response = RoomServiceProto.ErrorResponse.newBuilder()
                .setDescription(ex.getLocalizedMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RoomServiceProto.ErrorResponse> handle(
            UserNotFoundException ex, WebRequest request) {

        RoomServiceProto.ErrorResponse response = RoomServiceProto.ErrorResponse.newBuilder()
                .setDescription(ex.getLocalizedMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomWithExistingNameException.class)
    public ResponseEntity<RoomServiceProto.ErrorResponse> handle(
            RoomWithExistingNameException ex, WebRequest request) {

        RoomServiceProto.ErrorResponse response = RoomServiceProto.ErrorResponse.newBuilder()
                .setDescription(ex.getLocalizedMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SimilarRoomsExistException.class)
    public ResponseEntity<RoomServiceProto.ErrorResponse> handle(
            SimilarRoomsExistException ex, WebRequest request) {

        RoomServiceProto.ErrorResponse response = RoomServiceProto.ErrorResponse.newBuilder()
                .setDescription(ex.getLocalizedMessage())
                .addAllSimilarRooms(ex.getAlternativeRooms().stream().map(room -> mapper.map(room)).collect(Collectors.toList()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}