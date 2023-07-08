package ru.practicum.shareit.booking;

import java.util.Optional;

public enum StatusForSearch {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<StatusForSearch> from(String stringState) {
        for (StatusForSearch status : values()) {
            if (status.name().equalsIgnoreCase(stringState)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
