package ru.practicum.shareit.customException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    public void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void conflictExceptionTest() {
        ErrorResponse response = errorHandler.conflictException(new ConflictException("ConflictException"));
        assertEquals(response.getError(),"Конфликт: ");
        assertEquals(response.getDescription(),"ConflictException");
    }

    @Test
    void notFoundExceptionTest() {
        ErrorResponse response = errorHandler.notFoundException(new IllegalArgumentException("IllegalArgumentException"));
        assertEquals(response.getError(),"Ошибка ввода: ");
        assertEquals(response.getDescription(),"IllegalArgumentException");
    }

    @Test
    void validateArgumentExceptionTest() {
    //    ErrorResponse response = errorHandler.validateArgumentException(new MethodArgumentNotValidException("MethodArgumentNotValidException"));
    //    assertEquals(response.getError(),"Ошибка ввода: ");
    //    assertEquals(response.getDescription(),"IllegalArgumentException");
    }

    @Test
    void validateExceptionTest() {
        ErrorResponse response = errorHandler.validateException(new ValidationException("ValidationException"));
        assertEquals(response.getError(),"Ошибка валидации: ");
        assertEquals(response.getDescription(),"ValidationException");
    }

    @Test
    void exception1Test() {
        ErrorResponse response = errorHandler.exception(new Exception("Exception"));
        assertEquals(response.getError(),"Исключение: ");
        assertEquals(response.getDescription(),"Exception");
    }

    @Test
    void exception2Test() {
        ErrorResponseOne response = errorHandler.exception(new UnsopportedStatus("UnsopportedStatus"));
        assertEquals(response.getError(),"UnsopportedStatus");
    }
}