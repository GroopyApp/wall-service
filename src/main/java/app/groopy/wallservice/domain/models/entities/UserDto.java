package app.groopy.wallservice.domain.models.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
public class UserDto extends UserLiteDto {
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String gender;
    private List<String> subscribedTopics;
    private List<String> subscribedEvents;
}
