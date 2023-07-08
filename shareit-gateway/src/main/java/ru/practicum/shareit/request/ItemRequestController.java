package ru.practicum.shareit.request;

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
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemIequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemIequestClient) {
        this.itemIequestClient = itemIequestClient;
    }

    @PostMapping
    public ResponseEntity<Object> newItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Добавляется новый запрос вещи: {}", itemRequestDto);
        return itemIequestClient.newItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getMyItemRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Ищутся все request");
        return itemIequestClient.getMyItemRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PositiveOrZero
                                              @RequestParam(value = "from", required = false, defaultValue = "0")
                                              Integer from,
                                              @Positive
                                              @RequestParam(value = "size", required = false, defaultValue = "10")
                                              Integer size) {
        log.info("Ищется список запрос от пользователя с идентификатором: {}", userId);
        return itemIequestClient.getAllItemRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable Long requestId) {
        log.info("Ищется запрос по идентификатору: {}", requestId);
        return itemIequestClient.getItemRequest(userId, requestId);
    }
}


