package app.funfinder.roomservice.domain.models.common;

public enum Status {

    CREATED(200),
    UNKNOWN_ERROR(500);

    private final Integer code;

    Status(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
