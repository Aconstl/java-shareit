package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDtoIn;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.StatusForSearch;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.customException.UnsopportedStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceInDb;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class BookingServiceInDbTest {

    private BookingServiceInDb bookingService;

    @Mock
    private UserServiceInDb userService;

    @Mock
    private ItemServiceInDb itemService;

    @Mock
    private BookingRepositoryInDb bookingRepository;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceInDb(userService,itemService,bookingRepository);
    }

    @Test
    void create() {
        BookingDtoIn booking = BookingDtoIn.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1L)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(1L,booking));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        User user = new User(1L,"n","m@mail.com");
        when(userService.get(anyLong()))
                .thenReturn(user);
        Item item = new Item(1L,"item","description",true);
        item.setOwner(user);
        when(itemService.find(booking.getItemId()))
                .thenReturn(item);

        assertThrows(IllegalArgumentException.class, () -> bookingService.create(1L,booking));

        User user2 = new User(2L,"2n","2m@mail.com");
        when(userService.get(anyLong()))
                .thenReturn(user2);

        bookingService.create(2L,booking);

        item.setAvailable(false);
        when(itemService.find(booking.getItemId()))
                .thenReturn(item);
        assertThrows(ValidationException.class, () -> bookingService.create(1L,booking));

        verify(bookingRepository,times(1)).save(any(Booking.class));
    }

    @Test
    void changeStatus() {
        Item item = new Item(1L,"item","description",true);
        User userOwner = new User(1L,"owner","owner@mail.com");
        item.setOwner(userOwner);
        User userBooker = new User(2L,"booker","booker@mail.com");

        Booking booking = new Booking(item,userBooker,LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), Status.APPROVED);
        booking.setId(1L);

        when(bookingRepository.getStatusBooker(anyLong()))
                .thenReturn(Status.APPROVED);

        when(bookingRepository.getIdOwnerItem(anyLong()))
                .thenReturn(booking.getItem().getOwner().getId());

        assertThrows(ValidationException.class,
                () -> bookingService.changeStatus(userBooker.getId(),booking.getId(),null));

        assertThrows(ValidationException.class,
                () -> bookingService.changeStatus(userBooker.getId(),booking.getId(),true));

        when(bookingRepository.getStatusBooker(anyLong()))
                .thenReturn(Status.REJECTED);
        assertThrows(ValidationException.class,
                () -> bookingService.changeStatus(userBooker.getId(),booking.getId(),false));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.changeStatus(userBooker.getId(),booking.getId(),true));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        bookingService.changeStatus(userOwner.getId(),booking.getId(),true);
        when(bookingRepository.getStatusBooker(anyLong()))
                .thenReturn(Status.APPROVED);
        bookingService.changeStatus(userOwner.getId(),booking.getId(),false);
        verify(bookingRepository,times(2)).changeStatus(anyLong(),anyString());


    }

    @Test
    void get() {
        Item item = new Item(1L,"item","description",true);
        User userOwner = new User(1L,"owner","owner@mail.com");
        item.setOwner(userOwner);
        User userBooker = new User(2L,"booker","booker@mail.com");

        Booking booking = new Booking(item,userBooker,LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), Status.APPROVED);
        booking.setId(1L);

        when(bookingRepository.getIdOwnerItem(anyLong()))
                .thenReturn(1L);

        when(bookingRepository.getIdBooker(anyLong()))
                .thenReturn(2L);

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.get(1L,3L));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.get(null,1L));
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.get(0L,1L));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.get(1L,1L));

        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        bookingService.get(1L,1L);

    }

    @Test
    void getBookingUser() {
        when(userService.get(anyLong()))
                .thenReturn(new User());
        assertThrows(UnsopportedStatus.class,
                () -> bookingService.getBookingUser(1L,"uniq",null,null));

        when(bookingRepository.findAllByBookerIdOrderByIdDesc(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingUserCurrent(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingUserPast(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingUserFuture(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingUserStatus(anyLong(), anyString(),any(Pageable.class)))
                .thenReturn(Page.empty());

        verify(bookingRepository,times(0)).findAllByBookerIdOrderByIdDesc(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingUserCurrent(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingUserPast(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingUserFuture(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingUserStatus(1L,StatusForSearch.WAITING.toString(),Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingUserStatus(1L,StatusForSearch.REJECTED.toString(),Pageable.unpaged());

        bookingService.getBookingUser(1L, StatusForSearch.ALL.toString(),null,null);
        bookingService.getBookingUser(1L, StatusForSearch.CURRENT.toString(),null,null);
        bookingService.getBookingUser(1L, StatusForSearch.PAST.toString(),null,null);
        bookingService.getBookingUser(1L, StatusForSearch.FUTURE.toString(),null,null);
        bookingService.getBookingUser(1L, StatusForSearch.WAITING.toString(),null,null);
        bookingService.getBookingUser(1L, StatusForSearch.REJECTED.toString(),null,null);

        verify(bookingRepository,times(1)).findAllByBookerIdOrderByIdDesc(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingUserCurrent(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingUserPast(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingUserFuture(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingUserStatus(1L,StatusForSearch.WAITING.toString(),Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingUserStatus(1L,StatusForSearch.REJECTED.toString(),Pageable.unpaged());
    }

    @Test
    void getBookingOwner() {
        when(userService.get(anyLong()))
                .thenReturn(new User());
        assertThrows(UnsopportedStatus.class,
                () -> bookingService.getBookingOwner(1L,"uniq",null,null));

        when(bookingRepository.findBookingOwnerAll(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingOwnerCurrent(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingOwnerPast(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingOwnerFuture(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(bookingRepository.findBookingOwnerStatus(anyLong(), anyString(),any(Pageable.class)))
                .thenReturn(Page.empty());

        verify(bookingRepository,times(0)).findBookingOwnerAll(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingOwnerCurrent(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingOwnerPast(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingOwnerFuture(1L,Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingOwnerStatus(1L,StatusForSearch.WAITING.toString(),Pageable.unpaged());
        verify(bookingRepository,times(0)).findBookingOwnerStatus(1L,StatusForSearch.REJECTED.toString(),Pageable.unpaged());

        bookingService.getBookingOwner(1L, StatusForSearch.ALL.toString(),null,null);
        bookingService.getBookingOwner(1L, StatusForSearch.CURRENT.toString(),null,null);
        bookingService.getBookingOwner(1L, StatusForSearch.PAST.toString(),null,null);
        bookingService.getBookingOwner(1L, StatusForSearch.FUTURE.toString(),null,null);
        bookingService.getBookingOwner(1L, StatusForSearch.WAITING.toString(),null,null);
        bookingService.getBookingOwner(1L, StatusForSearch.REJECTED.toString(),null,null);

        verify(bookingRepository,times(1)).findBookingOwnerAll(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingOwnerCurrent(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingOwnerPast(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingOwnerFuture(1L,Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingOwnerStatus(1L,StatusForSearch.WAITING.toString(),Pageable.unpaged());
        verify(bookingRepository,times(1)).findBookingOwnerStatus(1L,StatusForSearch.REJECTED.toString(),Pageable.unpaged());
    }
}