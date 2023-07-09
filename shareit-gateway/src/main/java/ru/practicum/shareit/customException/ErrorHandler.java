package ru.practicum.shareit.customException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ErrorResponse validateArgumentException(final MethodArgumentNotValidException e) {
        log.error("Ошибка 400: {}", e.getMessage());
        return new ErrorResponse("Ошибка валидации: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(final ValidationException e) {
        log.error("Ошибка 400: {}", e.getMessage());
        return new ErrorResponse("Ошибка валидации: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseOne exception(final UnsopportedStatus u) {
        log.error("Ошибка 500: {}", u.getMessage());
        return new ErrorResponseOne(u.getMessage());
    }

}