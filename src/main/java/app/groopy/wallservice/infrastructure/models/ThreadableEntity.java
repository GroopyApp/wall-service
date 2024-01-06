package app.groopy.wallservice.infrastructure.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class ThreadableEntity extends Entity {
    List<ThreadEntity> threads;
}
