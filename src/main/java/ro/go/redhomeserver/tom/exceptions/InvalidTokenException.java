package ro.go.redhomeserver.tom.exceptions;

public class InvalidTokenException extends LogInException{
    public InvalidTokenException(String message) {
        super(message);
    }
}
