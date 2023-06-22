package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRequestServiceInDbIntegrationTest {

    @Autowired
    ItemRequestService itemRequestService;

    @Autowired
    UserService userService;

    @Test
    @DirtiesContext
    void newItemRequestAndGetTest() {
        User user2 = userService.create(UserDto.builder()
                .name("name2")
                .email("name2@mail.com")
                .build());

        ItemRequest itemRequest = itemRequestService.newItemRequest(user2.getId(),
                ItemRequestDto.builder()
                        .description("need Item 2")
                        .created(LocalDateTime.now())
                        .build());

        assertNotNull(itemRequest);
        assertEquals(itemRequest.getId(),1L);

        List<ItemRequest> listItemRequest = itemRequestService.getMyItemRequest(user2.getId());
        assertEquals(listItemRequest.size(),1);
        assertEquals(listItemRequest.get(0).getId(),1L);

        List<ItemRequest> allItemRequest = itemRequestService.getAllItemRequest(user2.getId(),null,null);
        assertTrue(allItemRequest.isEmpty());

        ItemRequest itemRequest1 = itemRequestService.getItemRequest(user2.getId(),1L);
        assertEquals(itemRequest1.getId(),1L);
        assertEquals(itemRequest1.getDescription(),"need Item 2");
    }
}