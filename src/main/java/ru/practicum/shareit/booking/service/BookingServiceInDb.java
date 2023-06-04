package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceInDb;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceInDb;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("BookingServiceInDb")
@Primary
public class BookingServiceInDb implements  BookingService {

    private final UserServiceInDb userService;
    private final ItemServiceInDb itemService;

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingDto bookingDto) {
      //  UserDto user = userService.get(userId);
        throw new UnsupportedOperationException("");
    }

    @Override
    @Transactional
    public BookingDto changeStatus(Long userId, Long bookingId, String approved){
        throw new UnsupportedOperationException("");
    }

    @Override
    @Transactional
    public BookingDto get(Long id) {
        throw new UnsupportedOperationException("");
    }

    @Override
    @Transactional
    public List<BookingDto> getBookingUser(Long userId, String state){
        throw new UnsupportedOperationException("");
    }

    @Override
    @Transactional
     public List<BookingDto> getBookingOwner(Long ownerId, String state){
         throw new UnsupportedOperationException("");
     }

}