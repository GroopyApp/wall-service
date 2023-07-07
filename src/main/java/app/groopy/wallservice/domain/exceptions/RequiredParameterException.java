package app.groopy.wallservice.domain.exceptions;

import lombok.Getter;

@Getter
public class RequiredParameterException extends Exception {

    private final String parameter;

    public RequiredParameterException(String parameter) {
        super(String.format("%s parameter is required", parameter));
        this.parameter = parameter;
    }
}
