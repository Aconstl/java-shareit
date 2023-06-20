package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void newUserTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();

        when(userService.create(Mockito.any(UserDto.class)))
                .thenAnswer( u -> {
                    UserDto userMock = u.getArgument(0, UserDto.class);
                    userMock.setId(1L);
                    return UserMapper.fromDto(userMock);
                });

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void getUserTest() throws Exception {

        when(userService.get(anyLong()))
                .thenReturn(new User(1L,"name","email@mail.com"));

        mvc.perform(get("/users/1")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void getAllTest() throws Exception {

        when(userService.getAll())
                .thenReturn(List.of(new User(1L,"name1","email1@mail.com"),
                        new User(2L,"name2","email2@mail.com") ));

        mvc.perform(get("/users/")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id",is(1L), Long.class))
                .andExpect(jsonPath("$.[1].id",is(2L), Long.class));
    }

    @Test
    public void updateUserTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("nameUpd")
                .email("emailUpd@mail.com")
                .build();

        when(userService.update(anyLong(),Mockito.any(UserDto.class)))
                .thenReturn(UserMapper.fromDto(userDto));

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));

        /*
        UserDto userDtoFail = UserDto.builder()
                .id(2L)
                .build();
        mvc.perform(patch("/users/2")
                        .content(mapper.writeValueAsString(userDtoFail))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());
        */
    }

    @Test
    public void deleteUserTest() throws Exception {

        mvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());
    }
}