package app.groopy.wallservice.domain.exceptions;

import app.groopy.wallservice.infrastructure.models.WallEntity;
import lombok.Getter;

@Getter
public class WallNotFoundException extends Exception {

    private final String id;
    private final String entityName = WallEntity.class.getSimpleName();

    public WallNotFoundException(String id, WallSearchType type) {
        super(String.format("Wall not found for %s %s", type.getType(), id));
        this.id = id;
    }

    public enum WallSearchType {

        ID("id"),
        LOCATION_ID("locationId");

        @Getter
        private String type;

        WallSearchType(String type) {
            this.type = type;
        }
    }
}
