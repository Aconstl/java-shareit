package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class BookingRepositoryInDbTest {

    @Autowired
    UserRepositoryInDb userRepository;

    @Autowired
    ItemRepositoryInDb itemRepository;

    @Autowired
    BookingRepositoryInDb bookingRepository;

    @Test
    @DirtiesContext
    public void testFindAllByBookerIdOrderByIdDesc() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        Item item2 = new Item(2L,"item2","description",true);
        item2.setOwner(userRepository.findById(2L).get());
        itemRepository.save(item2);

        LocalDateTime date = LocalDateTime.now();

        Booking booking1 = new Booking(item1,user2, date.minusDays(2),date.minusDays(1), Status.APPROVED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(item2,user2, date.plusDays(1),date.plusDays(1), Status.REJECTED);
        bookingRepository.save(booking2);

        List<Booking> booking = bookingRepository.findAllByBookerIdOrderByIdDesc(2L, Pageable.unpaged()).getContent();
        assertEquals(booking.size(),2);
        assertEquals(booking.get(0).getId(),2L);
        assertEquals(booking.get(0).getItem().getId(),2L);
        assertEquals(booking.get(1).getId(),1L);
        assertEquals(booking.get(1).getItem().getId(),1L);
    }

    @Test
    @DirtiesContext
    public void testFindAllByStatusAndItemInOrderByStartAsc() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        User user3 = new User(3L,"user3","user3@mail.ru");
        userRepository.save(user3);

        User user4 = new User(4L,"user4","user4@mail.ru");
        userRepository.save(user4);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();

        Booking booking1 = new Booking(item1,user2, date.plusDays(4),date.plusDays(5), Status.APPROVED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(item1,user3, date.plusDays(3),date.plusDays(4), Status.APPROVED);
        bookingRepository.save(booking2);

        Booking booking3 = new Booking(item1,user4, date.plusDays(1),date.plusDays(4), Status.REJECTED);
        bookingRepository.save(booking3);

        List<Booking> booking = bookingRepository.findAllByStatusAndItemInOrderByStartAsc(Status.APPROVED,List.of(item1));
        assertEquals(booking.size(),2);
        assertEquals(booking.get(0).getId(),2L);
        assertEquals(booking.get(1).getId(),1L);
        assertTrue(booking.get(0).getStart().isBefore(booking.get(1).getStart()));
    }

    @Test
    @DirtiesContext
    public void testFindBookingUserCurrentAndFutureAndPast() {
        User user = new User(1L, "user1", "user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L, "user2", "user2@mail.ru");
        userRepository.save(user2);

        Item item1 = new Item(1L, "item1", "description", true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();

        Booking booking1 = new Booking(item1, user2, date.minusDays(2), date.plusDays(2), Status.APPROVED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(item1, user2, date.plusDays(3), date.plusDays(4), Status.APPROVED);
        bookingRepository.save(booking2);

        Booking booking3 = new Booking(item1, user2, date.minusDays(4), date.minusDays(3), Status.REJECTED);
        bookingRepository.save(booking3);

        Booking booking4 = new Booking(item1, user2, date.plusDays(6), date.plusDays(7), Status.WAITING);
        bookingRepository.save(booking4);

        List<Booking> bookingCurrent = bookingRepository.findBookingUserCurrent(2L, Pageable.unpaged()).getContent();
        assertEquals(bookingCurrent.size(), 1);
        assertTrue(bookingCurrent.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookingCurrent.get(0).getEnd().isAfter(LocalDateTime.now()));

        List<Booking> bookingFuture = bookingRepository.findBookingUserFuture(2L, Pageable.unpaged()).getContent();
        assertEquals(bookingFuture.size(), 2);
        assertTrue(bookingFuture.get(0).getStart().isAfter(LocalDateTime.now()));
        assertTrue(bookingFuture.get(1).getStart().isAfter(LocalDateTime.now()));
        assertTrue(bookingFuture.get(0).getEnd().isAfter(bookingFuture.get(1).getEnd()));

        List<Booking> bookingPast = bookingRepository.findBookingUserPast(2L, Pageable.unpaged()).getContent();
        assertEquals(bookingPast.size(), 1);
        assertTrue(bookingPast.get(0).getEnd().isBefore(LocalDateTime.now()));

        List<Booking> bookingRejected = bookingRepository.findBookingUserStatus(2L, Status.REJECTED.toString(), Pageable.unpaged()).getContent();
        assertEquals(bookingRejected.size(), 1);
        assertEquals(bookingRejected.get(0).getStatus(), Status.REJECTED);
    }

    @Test
    @DirtiesContext
    public void testFindBookingOwnerAllAndCurrentAndFutureAndPast() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        User user3 = new User(3L,"user3","user3@mail.ru");
        userRepository.save(user3);

        User user4 = new User(4L,"user4","user4@mail.ru");
        userRepository.save(user4);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();

        Booking booking1 = new Booking(item1,user2, date.minusDays(2),date.plusDays(2), Status.APPROVED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(item1,user3, date.plusDays(3),date.plusDays(4), Status.APPROVED);
        bookingRepository.save(booking2);

        Booking booking3 = new Booking(item1,user4, date.minusDays(4),date.minusDays(3), Status.REJECTED);
        bookingRepository.save(booking3);

        Booking booking4 = new Booking(item1,user3, date.plusDays(6), date.plusDays(7), Status.WAITING);
        bookingRepository.save(booking4);

        List<Booking> bookingCurrent = bookingRepository.findBookingOwnerCurrent(1L,Pageable.unpaged()).getContent();
        assertEquals(bookingCurrent.size(),1);
        assertTrue(bookingCurrent.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookingCurrent.get(0).getEnd().isAfter(LocalDateTime.now()));

        List<Booking> bookingFuture = bookingRepository.findBookingOwnerFuture(1L,Pageable.unpaged()).getContent();
        assertEquals(bookingFuture.size(),2);
        assertTrue(bookingFuture.get(0).getStart().isAfter(LocalDateTime.now()));
        assertTrue(bookingFuture.get(1).getStart().isAfter(LocalDateTime.now()));
        assertTrue(bookingFuture.get(0).getEnd().isAfter(bookingFuture.get(1).getEnd()));

        List<Booking> bookingPast = bookingRepository.findBookingOwnerPast(1L,Pageable.unpaged()).getContent();
        assertEquals(bookingPast.size(),1);
        assertTrue(bookingPast.get(0).getEnd().isBefore(LocalDateTime.now()));

        List<Booking> bookingRejected = bookingRepository.findBookingOwnerStatus(1L,Status.REJECTED.toString(),Pageable.unpaged()).getContent();
        assertEquals(bookingPast.size(),1);
        assertEquals(bookingRejected.get(0).getStatus(), Status.REJECTED);
    }

    @Test
    @DirtiesContext
    public void testChangeStatus() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();
        Booking booking = new Booking(item1,user2, date.minusDays(2),date.plusDays(2), Status.WAITING);
        bookingRepository.save(booking);

        Booking bookingF = bookingRepository.findById(1L).get();
        assertEquals(bookingF.getStatus(),Status.WAITING);

        bookingRepository.changeStatus(1L,Status.APPROVED.toString());
        bookingF = bookingRepository.findById(1L).get();
        assertEquals(bookingF.getStatus(),Status.APPROVED);
    }

    @Test
    @DirtiesContext
    public void testIdOwnerItem() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();
        Booking booking = new Booking(item1,user2, date.minusDays(2),date.plusDays(2), Status.WAITING);
        bookingRepository.save(booking);

        Long ownerId = bookingRepository.getIdOwnerItem(1L);
        assertEquals(ownerId,1L);
        User userRes = userRepository.findById(1L).get();
        assertEquals(userRes.getName(),"user1");
    }

    @Test
    @DirtiesContext
    public void testGetIdBooker() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();
        Booking booking = new Booking(item1,user2, date.minusDays(2),date.plusDays(2), Status.WAITING);
        bookingRepository.save(booking);

        Long bookerId = bookingRepository.getIdBooker(1L);
        assertEquals(bookerId,2L);
        User bookerRes = userRepository.findById(2L).get();
        assertEquals(bookerRes.getName(),"user2");
    }

    @Test
    @DirtiesContext
    public void testGetStatusBooking() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();
        Booking booking = new Booking(item1,user2, date.minusDays(2),date.plusDays(2), Status.WAITING);
        bookingRepository.save(booking);

        Status status = bookingRepository.getStatusBooker(1L);
        assertEquals(status,Status.WAITING);
    }

    @Test
    @DirtiesContext
    public void testGetLastAndNextBooking() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        User user3 = new User(3L,"user3","user3@mail.ru");
        userRepository.save(user3);

        User user4 = new User(4L,"user4","user4@mail.ru");
        userRepository.save(user4);

        User user5 = new User(5L,"user5","user5@mail.ru");
        userRepository.save(user5);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();
        Booking booking1 = new Booking(item1,user2, date.minusDays(5),date.minusDays(3), Status.WAITING);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(item1,user3, date.plusDays(3),date.plusDays(4), Status.WAITING);
        bookingRepository.save(booking2);

        Booking booking3 = new Booking(item1,user4, date.minusDays(2),date.plusDays(2), Status.WAITING);
        bookingRepository.save(booking3);

        Booking booking4 = new Booking(item1,user4, date.plusDays(5),date.plusDays(6), Status.WAITING);
        bookingRepository.save(booking4);

        Booking lastBooking = bookingRepository.getLastBooking(1L);
        assertEquals(lastBooking.getId(),3L);
        assertTrue(lastBooking.getStart().isBefore(LocalDateTime.now()));

        Booking nextBooking = bookingRepository.getNextBooking(1L,booking3.getEnd());
        assertEquals(nextBooking.getId(),2L);
    }
}