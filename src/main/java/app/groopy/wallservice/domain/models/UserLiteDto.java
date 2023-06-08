package app.groopy.wallservice.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UserLiteDto {
    private String userId;
    private String name;
    private String surname;
    private String language;
}
