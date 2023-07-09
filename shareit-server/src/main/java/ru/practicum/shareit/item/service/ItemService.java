package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentDtoIn;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {
    Item create(Long userId, ItemDto itemDto);

    ItemDtoWithBooking get(Long id,Long userId);

    List<ItemDtoWithBooking> getAllItemUsers(Long userId, Long from,Long size);

    List<Item> search(String text, Long from, Long size);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    void delete(Long id);

    Comment postComment(Long userId, Long itemId, CommentDtoIn commentDtoIn);

}
