package ru.discomfortdeliverer.exception;

public class InvalidFilePathException extends Exception{
    public InvalidFilePathException() {
    }

    public InvalidFilePathException(String message) {
        super(message);
    }

    public InvalidFilePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFilePathException(Throwable cause) {
        super(cause);
    }
}
