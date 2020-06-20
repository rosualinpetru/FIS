package ro.go.redhomeserver.tom.exceptions;

import java.io.IOException;

public class FileStorageException extends IOException {
    public FileStorageException(String message, IOException e) {
        super(message, e);
    }
}
