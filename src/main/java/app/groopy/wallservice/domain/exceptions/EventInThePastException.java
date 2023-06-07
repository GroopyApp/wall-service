package app.groopy.wallservice.domain.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventInThePastException extends Exception {

    private LocalDateTime startDate;

    public EventInThePastException(LocalDateTime startDate) {
        super(String.format("startDate: %s is in the past", startDate.toString()));
        this.startDate = startDate;
    }
}
