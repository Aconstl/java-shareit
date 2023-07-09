package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> newItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Validated ItemDto itemDto) {
        log.trace("добавление предмета");
        return itemClient.newItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @PathVariable Long itemId) {
        log.trace("получение предмета");
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PositiveOrZero
                                                   @RequestParam (value = "from", required = false, defaultValue = "0")
                                                           Long from,
                                                  @Positive
                                                   @RequestParam (value = "size", required = false, defaultValue = "10")
                                                           Long size) {
        log.trace("вывод всех предметов пользователя");
        return itemClient.getAllItemUsers(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(value = "text", required = false) String text,
                                             @PositiveOrZero
                                             @RequestParam(value = "from", required = false, defaultValue = "0")
                                             Long from,
                                             @Positive
                                             @RequestParam(value = "size", required = false, defaultValue = "10")
                                             Long size) {
        log.trace("поиск предмета по имени");
        return itemClient.searchItem(userId, text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.trace("обновление предмета");
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        log.trace("удаление предмета");
        itemClient.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long itemId,
                                              @RequestBody @Valid CommentDtoIn commentDtoIn) {
        log.trace("Добавление комментария к предмету");
        return itemClient.postComment(userId, itemId, commentDtoIn);
    }

}
