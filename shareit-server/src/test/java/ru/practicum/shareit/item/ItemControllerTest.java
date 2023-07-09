package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentDtoIn;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void newItemTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .name("user1")
                .description("description")
                .available(true)
                .build();

        when(itemService.create(anyLong(),Mockito.any(ItemDto.class)))
                .thenAnswer(u -> {
                    ItemDto itemMock = u.getArgument(1, ItemDto.class);
                    itemMock.setId(1L);
                    return ItemMapper.fromDto(itemMock);
                });

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void getItemTest() throws Exception {

        when(itemService.get(anyLong(),anyLong()))
                .thenReturn(ItemDtoWithBooking.builder().id(1L).build());

        mvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void getAllItemTest() throws Exception {

        when(itemService.getAllItemUsers(anyLong(),anyLong(),anyLong()))
                .thenReturn(List.of(ItemDtoWithBooking.builder().id(1L).build(),
                        ItemDtoWithBooking.builder().id(2L).build()));

        mvc.perform(get("/items/")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1)
                        .param("from", String.valueOf(0))
                        .param("size",String.valueOf(0))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id",is(1L), Long.class))
                .andExpect(jsonPath("$.[1].id",is(2L), Long.class));
    }

    @Test
    public void searchItemTest() throws Exception {
        when(itemService.search(anyString(),anyLong(),anyLong()))
                .thenReturn(List.of(new Item(1L,"item1","desc1",true),
                        new Item(2L,"item2","desc2",true)));

        mvc.perform(get("/items/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", "any")
                        .param("from", String.valueOf(0))
                        .param("size",String.valueOf(0))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id",is(1L), Long.class))
                .andExpect(jsonPath("$.[1].id",is(2L), Long.class));
    }


    @Test
    public void updateItemTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .name("user1")
                .description("description")
                .available(true)
                .build();

        when(itemService.update(anyLong(),anyLong(),Mockito.any(ItemDto.class)))
                .thenAnswer(u -> {
                    ItemDto itemMock = u.getArgument(2, ItemDto.class);
                    itemMock.setId(1L);
                    return ItemMapper.fromDto(itemMock);
                });

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void deleteItemTest() throws Exception {

        mvc.perform(delete("/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void postCommentTest() throws Exception {
        CommentDtoIn commentIn = new CommentDtoIn();
        commentIn.setText("text");

        when(itemService.postComment(anyLong(),anyLong(),Mockito.any(CommentDtoIn.class)))
                .thenAnswer(u -> {
                    CommentDtoIn comMock = u.getArgument(2, CommentDtoIn.class);
                    Comment out = new Comment(comMock.getText(),new Item(),new User(),LocalDateTime.now());
                    out.setId(1L);
                    return out;
                });

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentIn))
                        .header("X-Sharer-User-Id",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class))
                .andExpect(jsonPath("$.text",is("text"), String.class));
    }
}