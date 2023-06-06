package ru.practicum.shareit.item.comment.model;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static Comment fromDto(CommentDtoIn commentDtoIn, Item item, User author, LocalDateTime created) {
        return new Comment(commentDtoIn.getText(),item,author,created);
    }

    public static CommentDtoOut toDto(Comment comment) {
        return CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDtoOut> toListDto(List<Comment> comments) {
        List<CommentDtoOut> commentDtoOut = new ArrayList<>();
        for (Comment c : comments) {
            commentDtoOut.add(CommentMapper.toDto(c));
        }
        return commentDtoOut;
    }
}
