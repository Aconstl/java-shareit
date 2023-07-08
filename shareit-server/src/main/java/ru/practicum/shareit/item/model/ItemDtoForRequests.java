package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDtoForRequests {

    private Long id;
    private String name;
    private Long ownerId;
}
