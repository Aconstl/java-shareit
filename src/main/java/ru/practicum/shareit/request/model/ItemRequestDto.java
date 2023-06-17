package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoForRequests;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {

    private Long id;

    @NotNull
    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;
}
