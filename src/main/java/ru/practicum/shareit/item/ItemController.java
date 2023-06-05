package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final BookingRepositoryInDb bookingRepository;
    @PostMapping
    public ItemDto newItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                        @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toDto(itemService.create(userId,itemDto));
    }

    @GetMapping("/{id}")
    public ItemDtoWithBooking getItem(@PathVariable Long id,
                                      @RequestHeader (name = "X-Sharer-User-Id",defaultValue = "-1") Long userId) {
        return itemService.get(id,userId);
    }

    @GetMapping
    public List<ItemDtoWithBooking> getAllItemUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return (itemService.getAllItemUsers(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(value = "text") String text) {
        return ItemMapper.toListDto(itemService.search(text));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long id,
                           @RequestBody ItemDto itemDto) {
        return ItemMapper.toDto(itemService.update(userId,id,itemDto));
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.delete(id);
    }

}


