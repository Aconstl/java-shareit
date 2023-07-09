package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Pagination;
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
        /*
        if (bookingDtoIn.getStart().isAfter(bookingDtoIn.getEnd()) ||
                bookingDtoIn.getStart().isEqual(bookingDtoIn.getEnd())) {
            throw new ValidationException("время бронирования указано некорректно");
        }
        */
        User user = userService.get(userId);
        Item item = itemService.find(bookingDtoIn.getItemId());
        if (item.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Пользователь предмета не может бронировать собственный предмет");
        }
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
        if ((approved == null) ||
                (bookingRepository.getStatusBooker(bookingId) == Status.APPROVED && approved) ||
                (bookingRepository.getStatusBooker(bookingId) == Status.REJECTED && !approved)) {
            throw new ValidationException("Подтверджение указано некорректно");
        }
        if (userId.equals(bookingRepository.getIdOwnerItem(bookingId))) {
                if (approved) {
                    bookingRepository.changeStatus(bookingId, Status.APPROVED.toString());
                } else {
                    bookingRepository.changeStatus(bookingId,Status.REJECTED.toString());
                }
                return findBooking(bookingId);
        }
        throw new IllegalArgumentException("Пользователь не является владельцем бронируемого предмета");
    }

    private Booking findBooking(Long bookingId) {
        log.trace("получение данных бронирования");
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new IllegalArgumentException("Бронирование с Id № " + bookingId + " не найдено");
        }
        log.debug("Бронирование с id №{} получено", bookingId);
        return booking.get();
    }

    @Override
    public Booking get(Long bookingId, Long userId) {
        log.trace("получение данных бронирования");
        Long idOwner = bookingRepository.getIdOwnerItem(bookingId);
        Long idUser = bookingRepository.getIdBooker(bookingId);
        if (userId.equals(idUser) || userId.equals(idOwner)) {
            return findBooking(bookingId);
        }
        throw new IllegalArgumentException("Пользователь не является автором запроса или хозяйном предмета");
    }

    @Override
    public List<Booking> getBookingUser(Long userId, String state, Long from, Long size) {
        log.trace("Получение бронирования пользователя");
        userService.get(userId);
        Pageable pageable = Pagination.setPageable(from,size);
        StatusForSearch status = StatusForSearch.valueOf(state);
        switch (status) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByIdDesc(userId, pageable).getContent();
            case CURRENT:
                return bookingRepository.findBookingUserCurrent(userId, pageable).getContent();
            case PAST:
                return bookingRepository.findBookingUserPast(userId, pageable).getContent();
            case FUTURE:
                return bookingRepository.findBookingUserFuture(userId, pageable).getContent();
            default:
                return bookingRepository.findBookingUserStatus(userId, status.toString(), pageable).getContent();
        }
    }

    @Override
     public List<Booking> getBookingOwner(Long ownerId, String state, Long from, Long size) {
        log.trace("Получение бронирования владельца");
        userService.get(ownerId);
        Pageable pageable = Pagination.setPageable(from, size);
        StatusForSearch status = StatusForSearch.valueOf(state);
        switch (status) {
            case ALL:
                return bookingRepository.findBookingOwnerAll(ownerId, pageable).getContent();
            case CURRENT:
                return bookingRepository.findBookingOwnerCurrent(ownerId, pageable).getContent();
            case PAST:
                return bookingRepository.findBookingOwnerPast(ownerId, pageable).getContent();
            case FUTURE:
                return bookingRepository.findBookingOwnerFuture(ownerId, pageable).getContent();
            default:
                return bookingRepository.findBookingOwnerStatus(ownerId, status.toString(), pageable).getContent();
        }
    }

}