package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDtoForBooking {

        private Long id; // УНИКАЛЬНЫЙ id пользователя

}

