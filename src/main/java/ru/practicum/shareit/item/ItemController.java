package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto newItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                        @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId,itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Integer id) {
        return itemService.get(id);
    }

    @GetMapping
    public List<ItemDto> getAllItemUsers(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllItemUsers(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(value = "text") String text) {
        return itemService.search(text);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer id,
                           @RequestBody ItemDto itemDto) {
        return itemService.update(userId,id,itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.delete(id);
    }

}


