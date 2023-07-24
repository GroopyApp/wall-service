package app.groopy.wallservice.domain.models.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UserLiteDto extends BasicEntityDto {
    private String userId;
    private String name;
    private String surname;
    private String language;
    private String photoUrl;
}
