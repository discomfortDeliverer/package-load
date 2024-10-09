package ru.discomfortdeliverer.exception;

public class UnknownPackageException extends RuntimeException{
    public UnknownPackageException() {
    }

    public UnknownPackageException(String message) {
        super(message);
    }

    public UnknownPackageException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownPackageException(Throwable cause) {
        super(cause);
    }
}
