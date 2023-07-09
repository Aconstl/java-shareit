package ru.practicum.shareit.customException;

public class UnsopportedStatus extends RuntimeException {

    public UnsopportedStatus(String message) {
        super(message);
    }
}