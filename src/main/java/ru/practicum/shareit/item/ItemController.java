package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
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
    public Item newItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                        @Valid @RequestBody Item item) {
        return itemService.newItem(userId,item);
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable Integer id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public List<Item> getAllItemUsers(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAllItemUsers(userId);
    }

    @GetMapping("/search")
    public List<Item> searchFilm(@RequestParam(value = "text", defaultValue = "", required = false) String text) {
        return itemService.searchFilm(text);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                           @PathVariable Integer id,
                           @RequestBody Item item) {
        return itemService.updateItem(userId,id,item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

}


