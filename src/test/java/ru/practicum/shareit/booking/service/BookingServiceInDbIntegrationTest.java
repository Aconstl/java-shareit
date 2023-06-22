package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDtoIn;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceInDbIntegrationTest {

    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Test
    public void createBookingAndChangeStatusAndGetTest() {
        User user1 = userService.create(UserDto.builder()
                .name("name1")
                .email("name1@mail.com")
                .build());

        User user2 = userService.create(UserDto.builder()
                .name("name2")
                .email("name2@mail.com")
                .build());

        Item item1 = itemService.create(user1.getId(),
                ItemDto.builder()
                        .name("item")
                        .description("description")
                        .available(true)
                        .build());


        Booking booking = bookingService.create(user2.getId(), BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build());

        assertNotNull(booking);

        Booking bookingGetter = bookingService.get(booking.getId(),user1.getId());
        assertNotNull(bookingGetter);
        assertEquals(booking.getStatus(), Status.WAITING);
        assertEquals(booking.getId(),bookingGetter.getId());

        Booking bookingChangerStatus = bookingService.changeStatus(user1.getId(),booking.getId(),true);
        assertEquals(bookingChangerStatus.getStatus(), Status.APPROVED);
    }
}