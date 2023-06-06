package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.customException.UnsopportedStatus;
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
        if (bookingId == null || bookingId == 0) {
            throw new NullPointerException("Id бронирования указан неверно");
        }
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new IllegalArgumentException("Бронирование с Id № " + bookingId + " не найдено");
        }
        log.debug("Бронирование с id №{} получено", bookingId);
        return booking.get();
    }

    @Override
    @Transactional
    public Booking get(Long bookingId, Long userId) {
        log.trace("получение данных бронирования");
        Long idOwner = bookingRepository.getIdOwnerItem(bookingId);
        Long idUser = bookingRepository.getIdBooker(bookingId);
        if (userId.equals(idUser) || userId.equals(idOwner)) {
            return findBooking(bookingId);
        }
        throw new IllegalArgumentException ("Пользователь не является автором запроса или хозяйном предмета");
    }

    @Override
    @Transactional
    public List<Booking> getBookingUser(Long userId, String state){
        log.trace("Получение бронирования пользователя");
        userService.get(userId);
        try {
            StatusForSearch status = StatusForSearch.valueOf(state);
        switch (status) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByIdDesc(userId);
            case CURRENT:
                return bookingRepository.findBookingUserCurrent(userId);
            case PAST:
                return bookingRepository.findBookingUserPast(userId);
            case FUTURE:
                return bookingRepository.findBookingUserFuture(userId);
            default:
                return bookingRepository.findBookingUserStatus(userId,status.toString());
        }
        } catch (Exception e) {
            throw new UnsopportedStatus("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    @Transactional
     public List<Booking> getBookingOwner(Long ownerId, String state){
        log.trace("Получение бронирования владельца");
        userService.get(ownerId);
        try {
            StatusForSearch status = StatusForSearch.valueOf(state);
            switch (status) {
                case ALL:
                    return bookingRepository.findBookingOwnerAll(ownerId);
                case CURRENT:
                    return bookingRepository.findBookingOwnerCurrent(ownerId);
                case PAST:
                    return bookingRepository.findBookingOwnerPast(ownerId);
                case FUTURE:
                    return bookingRepository.findBookingOwnerFuture(ownerId);
                default:
                    return bookingRepository.findBookingOwnerStatus(ownerId,status.toString());
            }
        } catch (Exception e) {
            throw new UnsopportedStatus("Unknown state: UNSUPPORTED_STATUS");
        }
     }

}