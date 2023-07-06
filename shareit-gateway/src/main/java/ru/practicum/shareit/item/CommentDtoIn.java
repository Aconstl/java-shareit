package ru.practicum.shareit.item;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CommentDtoIn {

    @Size(max = 500)
    private String text;
}
