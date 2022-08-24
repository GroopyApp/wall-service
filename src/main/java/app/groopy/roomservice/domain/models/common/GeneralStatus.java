package app.groopy.roomservice.domain.models.common;

public enum GeneralStatus {

    OK(200),
    UNKNOWN_ERROR(500),
    COMPLETED(201);

    private final Integer code;

    GeneralStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
