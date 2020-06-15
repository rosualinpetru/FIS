package ro.go.redhomeserver.tom.exceptions;

public class UserNotFoundException extends LogInException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
