package ru.practicum.shareit.customException;

public class ErrorResponseOne {
    private final String error;


    public ErrorResponseOne(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
