package ro.go.redhomeserver.tom.exceptions;

public class WeakPasswordException extends SignUpException {
    public WeakPasswordException(String message) {
        super(message);
    }
}
