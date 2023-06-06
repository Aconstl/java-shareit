package ru.practicum.shareit.item.comment.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CommentDtoIn {

    @Size(max = 500)
    String text;
}
