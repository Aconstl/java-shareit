package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDtoIn;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void newBookingTest() throws Exception {
        BookingDtoIn bookingIn = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();

        when(bookingService.create(anyLong(), Mockito.any(BookingDtoIn.class)))
                .thenAnswer(u -> {
                    BookingDtoIn itemMock = u.getArgument(1, BookingDtoIn.class);
                    Booking booking = new Booking(new Item(),new User(),itemMock.getStart(),
                            itemMock.getEnd(), Status.WAITING);
                    booking.setId(1L);
                    return booking;
                });

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingIn))
                        .header("X-Sharer-User-Id",1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void changeStatusTest() throws Exception {
        BookingDtoIn bookingIn = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();

        when(bookingService.changeStatus(anyLong(),anyLong(),anyBoolean()))
                .thenAnswer(u -> {
                    Booking booking = new Booking(new Item(),new User(),bookingIn.getStart(),
                            bookingIn.getEnd(), Status.APPROVED);
                    booking.setId(1L);
                    return booking;
                });

        mvc.perform(patch("/bookings/1")
                      //  .content(mapper.writeValueAsString(bookingIn))
                        .header("X-Sharer-User-Id",1)
                        .param("approved",String.valueOf(true))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void getBookingTest() throws Exception {
        BookingDtoIn bookingIn = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();

        when(bookingService.get(anyLong(),anyLong()))
                .thenAnswer(u -> {
                    Booking booking = new Booking(new Item(),new User(),bookingIn.getStart(),
                            bookingIn.getEnd(), Status.APPROVED);
                    booking.setId(1L);
                    return booking;
                });

        mvc.perform(get("/bookings/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1L), Long.class));
    }

    @Test
    public void getBookingUserTest() throws Exception {
        BookingDtoIn bookingIn = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();

        when(bookingService.getBookingUser(anyLong(),anyString(),anyLong(),anyLong()))
                .thenAnswer(u -> {
                    Booking booking1 = new Booking(new Item(),new User(),bookingIn.getStart(),
                            bookingIn.getEnd(), Status.APPROVED);
                    booking1.setId(1L);
                    Booking booking2 = new Booking(new Item(),new User(),bookingIn.getStart(),
                            bookingIn.getEnd(), Status.APPROVED);
                    booking2.setId(2L);
                    return List.of(booking1,booking2);
                });

        mvc.perform(get("/bookings")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1)
                        .param("text", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size",String.valueOf(0))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id",is(1L), Long.class))
                .andExpect(jsonPath("$.[1].id",is(2L), Long.class));
    }

    @Test
    public void getBookingOwnerTest() throws Exception {
        BookingDtoIn bookingIn = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();

        when(bookingService.getBookingOwner(anyLong(),anyString(),anyLong(),anyLong()))
                .thenAnswer(u -> {
                    Booking booking1 = new Booking(new Item(),new User(),bookingIn.getStart(),
                            bookingIn.getEnd(), Status.APPROVED);
                    booking1.setId(1L);
                    Booking booking2 = new Booking(new Item(),new User(),bookingIn.getStart(),
                            bookingIn.getEnd(), Status.APPROVED);
                    booking2.setId(2L);
                    return List.of(booking1,booking2);
                });

        mvc.perform(get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id",1)
                        .param("text", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size",String.valueOf(0))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id",is(1L), Long.class))
                .andExpect(jsonPath("$.[1].id",is(2L), Long.class));
    }

}