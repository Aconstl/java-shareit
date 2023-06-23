package ru.practicum.shareit.item.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepositoryInDb extends JpaRepository<Comment,Long> {

    List<Comment> findAllByItemIn(List<Item> items);

    List<Comment> findAllByItemId(Long itemId);
}
