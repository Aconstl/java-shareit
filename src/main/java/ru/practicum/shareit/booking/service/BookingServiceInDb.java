package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceInDb;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("BookingServiceInDb")
@Primary
public class BookingServiceInDb implements  BookingService {

    private final UserServiceInDb userService;
    private final ItemServiceInDb itemService;

    private final BookingRepositoryInDb bookingRepository;

    @Override
    @Transactional
    public Booking create(Long userId, BookingDtoIn bookingDtoIn) {
        log.trace("создание бронирования");
        if (bookingDtoIn.getStart().isAfter(bookingDtoIn.getEnd()) ||
                bookingDtoIn.getStart().isEqual(bookingDtoIn.getEnd())) {
            throw new ValidationException("время бронирования указано некорректно");
        }
        User user = userService.get(userId);
        Item item = itemService.get(bookingDtoIn.getItemId());
        if (item.getAvailable()) {
            Booking booking = BookingMapper.fromDto(item, user, bookingDtoIn, Status.WAITING);
            return bookingRepository.save(booking);
        }  else {
            throw new ValidationException("предмет недоступен для бронирования");
        }
    }

    @Override
    @Transactional
    public Booking changeStatus(Long userId, Long bookingId, Boolean approved) {
        log.trace("смена статуса бронирования");
        Long ownerItemId = bookingRepository.getIdOwnerItem(bookingId);
        if (userId.equals(ownerItemId)) {
            if (approved != null) {
                if (approved) {
                    bookingRepository.changeStatus(bookingId, Status.APPROVED.toString());
                } else {
                    bookingRepository.changeStatus(bookingId,Status.REJECTED.toString());
                }
                return get(bookingId);
            }
            throw new ValidationException("Подтверджение указано некорректно");
        }
        throw new IllegalArgumentException("Пользователь не является владельцем бронируемого предмета");

    }

    @Override
    @Transactional
    public Booking get(Long id) {
        log.trace("получение данных бронирования");
        if (id == null || id == 0) {
            throw new NullPointerException("Id бронирования указан неверно");
        }
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new IllegalArgumentException("Бронирование с Id № " + id + " не найдено");
        }
        log.debug("Бронирование с id №{} получено", id);
        return booking.get();
    }

    @Override
    @Transactional
    public List<Booking> getBookingUser(Long userId, String state){
        User user = userService.get(userId);
        if (state.equals(StatusForSearch.All.toString())) {
            return bookingRepository.findAll();
        }
        throw new UnsupportedOperationException("");
    }

    @Override
    @Transactional
     public List<Booking> getBookingOwner(Long ownerId, String state){
         throw new UnsupportedOperationException("");
     }

}