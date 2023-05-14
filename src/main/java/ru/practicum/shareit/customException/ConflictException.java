package ru.practicum.shareit.customException;

public class ConflictException extends RuntimeException {
    public ConflictException (String message) {
        super(message);
    }
}
