package ru.practicum.shareit.booking.model;

public enum Status {
    WAITING,    // ожидает одобрения
    APROVED,    // подтверждено владельцем
    REJECTED,   // отклонено владельцем
    CANCELED    // отменено создателем
}
