package app.groopy.wallservice.domain.exceptions;

import app.groopy.wallservice.infrastructure.models.WallEntity;
import lombok.Getter;

@Getter
public class WallNotFoundException extends Exception {

    private final String locationId;
    private final String entityName = WallEntity.class.getSimpleName();

    public WallNotFoundException(String locationId) {
        super(String.format("Wall not found for locationId %s", locationId));
        this.locationId = locationId;
    }
}
