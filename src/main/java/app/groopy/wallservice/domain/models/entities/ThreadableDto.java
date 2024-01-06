package app.groopy.wallservice.domain.models.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class ThreadableDto extends BasicEntityDto {
    List<String> threads;
}
