package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.item.comment.repository.CommentRepositoryInDb;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoWithBooking;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryInDb;
import ru.practicum.shareit.request.service.ItemRequestServiceInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.service.UserServiceInDb;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceInDbTest {

    private ItemService itemService;

    @Mock
    ItemRepositoryInDb itemRepository;

    @Mock
    UserServiceInDb userService;

    @Mock
    BookingRepositoryInDb bookingRepository;

    @Mock
    CommentRepositoryInDb commentRepository;

    @Mock
    ItemRequestServiceInDb itemRequestService;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceInDb(itemRepository,userService,bookingRepository,commentRepository,
                itemRequestService);
    }

    @Test
    public void createAndGetItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .name("nameItem")
                .description("descriptionItem")
                .available(true)
                .build();

        Long userId = 1L;

        when(userService.get(userId))
                .thenReturn(new User(1L,"user1","user1@mail.ru"));

        itemService.create(userId,itemDto);
    // добавить сразу логику на бронирование, комментарии
        ItemDtoWithBooking item = itemService.get(1L,1L);
    }
}