package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoWithBooking;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceInDbIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Test
    @DirtiesContext
    public void createItemAndGetTest() {
        User user = userService.create(UserDto.builder()
                .name("nameUser")
                .email("nameUser@mail.com")
                .build());

        Item item1 = itemService.create(user.getId(),
                ItemDto.builder()
                        .name("item")
                        .description("description")
                        .available(true)
                        .build());

        assertNotNull(item1);
        assertEquals(item1.getName(),"item");

        ItemDtoWithBooking itemGetter = itemService.get(item1.getId(),user.getId());
        assertNotNull(itemGetter);
        assertEquals(item1.getId(),itemGetter.getId());
        assertEquals(item1.getName(),itemGetter.getName());

        itemService.delete(item1.getId());
        List<ItemDtoWithBooking> items = itemService.getAllItemUsers(user.getId(),null,null);
        assertTrue(items.isEmpty());
    }
}