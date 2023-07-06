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
    public ResponseEntity<Object> newItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemDto itemDto) {
        log.info("Добавляется вещь: {}", itemDto);
        return itemClient.newItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @PathVariable Long itemId) {
        log.info("Ищется вещь по идентификатору: {}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findItemByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PositiveOrZero
                                                   @RequestParam (value = "from", required = false, defaultValue = "0")
                                                           Integer from,
                                                   @Positive
                                                   @RequestParam (value = "size", required = false, defaultValue = "10")
                                                           Integer size) {
        log.info("Ищется вещь по пользователю: {}", userId);
        return itemClient.findItemByUserId(userId, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patch(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId,
                                        @RequestBody ItemDto itemDto) {
        log.info("Обновляется вещь по идентификатору: {}", itemId);
        return itemClient.patchItem(userId, itemId, itemDto);
    }

"/search"
    public ResponseEntity<Object> getBySearch(@RequestHeader("X-Sharer-User-Id") long userId,
                                              value = "text", required = false String text,
                                              @PositiveOrZero
                                              value = "from", required = false, defaultValue = "0"
                                                      Integer from,
                                              @Positive
                                              value = "size", required = false, defaultValue = "10"
                                                      Integer size) {
        log.info("Ищется вещь по параметру: {}", text);
        return itemClient.getItemBySearch(userId, text, from, size);
    }

"/{itemId}/comment"
    public ResponseEntity<Object> addComment("X-Sharer-User-Id" long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid AddCommentDto addCommentDto) {
        log.info("Добавляется комментарий: {}", addCommentDto);
        return itemClient.addComment(userId, itemId, addCommentDto);
    }

}
