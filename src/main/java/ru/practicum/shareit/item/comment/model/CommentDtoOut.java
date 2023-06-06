package ru.practicum.shareit.item.comment.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoOut {

    Long id;

    String text;

    String authorName;

    LocalDateTime created;

}
