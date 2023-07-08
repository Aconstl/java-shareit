package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.shareit.item.ItemDto;

@Data
@Builder
public class ItemRequestDto {

    private Long id;

    @NotNull
    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;
}
