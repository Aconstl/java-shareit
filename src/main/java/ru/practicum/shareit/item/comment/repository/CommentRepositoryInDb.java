package ru.practicum.shareit.item.comment.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommentRepositoryInDb extends JpaRepository<Comment,Long> {

    List<Comment> findAllByItemIn(List<Item> items);

    List<Comment> findAllByItemId(Long itemId);
}
