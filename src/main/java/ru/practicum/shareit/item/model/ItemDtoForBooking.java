package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDtoForBooking {
    private Long id;
    @NotBlank
    private String name;
//    @NotBlank
//   private Long ownerId;
}

