package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class ItemRepositoryInDbTest {
    @Autowired
    ItemRepositoryInDb itemRepository;

    @Autowired
    UserRepositoryInDb userRepository;

    @Autowired
    BookingRepositoryInDb bookingRepository;

    @Test
    @DirtiesContext
    public void testFindByOwner_IdByIdAsc() {
        userRepository.save(new User(1L,"user1","user1@mail.ru"));
        userRepository.save(new User(2L,"user2","user2@mail.ru"));

        Item item1 = new Item(1L,"item1","itemDecription1",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        Item item2 = new Item(2L,"item2","itemDecription2",true);
        item2.setOwner(userRepository.findById(2L).get());
        itemRepository.save(item2);

        Item item3 = new Item(3L,"item3","itemDecription3",true);
        item3.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item3);

        Page<Item> items = itemRepository.findByOwner_IdOrderByIdAsc(1L,Pageable.unpaged());
        List<Item> itemList = items.getContent();
        assertEquals(itemList.size(),2);

        assertEquals(itemList.get(0).getId(),item1.getId());
        assertEquals(itemList.get(0).getName(),item1.getName());
        assertEquals(itemList.get(0).getDescription(),item1.getDescription());
        assertEquals(itemList.get(0).getOwner().getId(),item1.getOwner().getId());

        assertEquals(itemList.get(1).getId(),item3.getId());
        assertEquals(itemList.get(1).getName(),item3.getName());
        assertEquals(itemList.get(1).getDescription(),item3.getDescription());
        assertEquals(itemList.get(1).getOwner().getId(),item3.getOwner().getId());
    }

    @Test
    @DirtiesContext
    public void testSearchItem() {
        userRepository.save(new User(1L,"user1","user1@mail.ru"));

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        Item item2 = new Item(2L,"itemTwo","DESCRIPTION",true);
        item2.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item2);

        Item item3 = new Item(3L,"item3","DESCRIPTION",false);
        item3.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item3);

        Page<Item> itemsRes1 = itemRepository.searchItem("itemtwo",Pageable.unpaged());
        List<Item> itemList1 = itemsRes1.getContent();
        assertEquals(itemList1.size(),1);
        assertEquals(itemList1.get(0).getId(),item2.getId());
        assertEquals(itemList1.get(0).getName(),item2.getName());
        assertEquals(itemList1.get(0).getDescription(),item2.getDescription());
        assertEquals(itemList1.get(0).getOwner().getId(),item2.getOwner().getId());

        Page<Item> itemsRes2 = itemRepository.searchItem("description",Pageable.unpaged());
        List<Item> itemList2 = itemsRes2.getContent();
        assertEquals(itemList2.size(),2);
        assertEquals(itemList2.get(0).getId(),item1.getId());
        assertEquals(itemList2.get(0).getName(),item1.getName());
        assertEquals(itemList2.get(0).getDescription(),item1.getDescription());
        assertEquals(itemList2.get(0).getOwner().getId(),item1.getOwner().getId());

        assertEquals(itemList2.get(1).getId(),item2.getId());
        assertEquals(itemList2.get(1).getName(),item2.getName());
        assertEquals(itemList2.get(1).getDescription(),item2.getDescription());
        assertEquals(itemList2.get(1).getOwner().getId(),item2.getOwner().getId());
    }


    @Test
    @DirtiesContext
    public void testGetOwnerId() {
        userRepository.save(new User(1L,"user1","user1@mail.ru"));
        userRepository.save(new User(2L,"user2","user2@mail.ru"));

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(2L).get());
        itemRepository.save(item1);

        Item item2 = new Item(2L,"item2","description",true);
        item2.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item2);

        Long idUser1 = itemRepository.getOwnerId(2L);
        assertEquals(idUser1,1L);

        Long idUser2 = itemRepository.getOwnerId(1L);
        assertEquals(idUser2,2L);
    }

    @Test
    @DirtiesContext
    public void testUpdateNameAndDescriptionAndAvailable() {
        userRepository.save(new User(1L,"user1","user1@mail.ru"));

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        Item itemRes = itemRepository.findById(1L).get();
        assertEquals(itemRes.getId(),1L);
        assertEquals(itemRes.getName(),"item1");
        assertEquals(itemRes.getDescription(),"description");
        assertEquals(itemRes.getAvailable(),true);

        itemRepository.updateName(1L, "itemUpd");
        itemRes = itemRepository.findById(1L).get();
        assertEquals(itemRes.getId(),1L);
        assertEquals(itemRes.getName(),"itemUpd");

        itemRepository.updateDescription(1L, "descUpd");
        itemRes = itemRepository.findById(1L).get();
        assertEquals(itemRes.getId(),1L);
        assertEquals(itemRes.getDescription(),"descUpd");

        itemRepository.updateAvailable(1L, false);
        itemRes = itemRepository.findById(1L).get();
        assertEquals(itemRes.getId(),1L);
        assertEquals(itemRes.getAvailable(),false);
    }

    @Test
    @DirtiesContext
    public void testFindBookingUser() {
        User user = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        User user3 = new User(3L,"user3","user3@mail.ru");
        userRepository.save(user3);

        Item item1 = new Item(1L,"item1","description",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        Item item2 = new Item(2L,"item2","description",true);
        item2.setOwner(userRepository.findById(2L).get());
        itemRepository.save(item2);

        LocalDateTime date = LocalDateTime.now();
        Booking bookingFuture = new Booking(item1,user2, date.plusDays(2),date.plusDays(1), Status.WAITING);
        bookingRepository.save(bookingFuture);

        Booking booking1 = new Booking(item1,user3, date.minusDays(2),date.minusDays(1), Status.APPROVED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(item2,user3, date.minusDays(2),date.minusDays(1), Status.REJECTED);
        bookingRepository.save(booking2);

        Item itemRes1 = itemRepository.findBookingUser(2L,1L);
        assertNull(itemRes1);

        Item itemRes2 = itemRepository.findBookingUser(3L,1L);
        assertNotNull(itemRes2);
        assertEquals(itemRes2.getId(),1L);

        Item itemRes3 = itemRepository.findBookingUser(3L,2L);
        assertNull(itemRes3);
    }
}