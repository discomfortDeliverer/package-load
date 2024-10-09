package ru.discomfortdeliverer.exception.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.discomfortdeliverer.exception.ParcelNotFoundException;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.exception.UnableUpdateParcelException;
import ru.discomfortdeliverer.exception.UnknownPackageException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParcelNotFoundException.class)
    public ResponseEntity<String> handleParcelNotFoundException(ParcelNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnableUpdateParcelException.class)
    public ResponseEntity<String> handleUnableUpdateParcelException(UnableUpdateParcelException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UnknownPackageException.class)
    public ResponseEntity<String> handleUnknownPackageException(UnknownPackageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UnableToLoadException.class)
    public ResponseEntity<String> handleUnableToLoadException(UnableToLoadException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
