package app.groopy.roomservice.domain.exceptions;

public class UserNotFoundException extends Throwable {

    public UserNotFoundException(String userId) {
        super("user not found with id: " + userId);
    }
}
