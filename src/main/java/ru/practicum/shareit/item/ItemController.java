package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto newItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                        @Valid @RequestBody ItemDto itemDto) {
        return itemService.newItem(userId,itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Integer id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public List<ItemDto> getAllItemUsers(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllItemUsers(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchFilm(@RequestParam(value = "text", defaultValue = "", required = false) String text) {
        return itemService.searchFilm(text);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer id,
                           @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId,id,itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

}


