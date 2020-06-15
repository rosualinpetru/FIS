package ro.go.redhomeserver.tom.exceptions;

public class PasswordVerificationException extends SignUpException{
    public PasswordVerificationException(String message) {
        super(message);
    }
}
