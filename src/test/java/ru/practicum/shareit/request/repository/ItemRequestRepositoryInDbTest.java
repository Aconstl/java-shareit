package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryInDbTest {

    @Autowired
    UserRepositoryInDb userRepository;

    @Autowired
    ItemRepositoryInDb itemRepository;

    @Autowired
    ItemRequestRepositoryInDb itemRequestRepository;

    @Test
    @DirtiesContext
    public void testFindByAuthor_IdOrderByCreatedAsc() {
        User user1 = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user1);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        ItemRequest request1 = new ItemRequest("itemReq1",user1, LocalDateTime.now());
        itemRequestRepository.save(request1);

        ItemRequest request2 = new ItemRequest("itemReq2",user2, LocalDateTime.now().minusDays(1));
        itemRequestRepository.save(request2);

        ItemRequest request3 = new ItemRequest("itemReq3",user2, LocalDateTime.now().minusDays(2));
        itemRequestRepository.save(request3);

        List<ItemRequest> requests = itemRequestRepository.findByAuthor_IdOrderByCreatedAsc(2L);
        assertEquals(requests.size(),2);
        assertEquals(requests.get(0).getId(),3L);
        assertEquals(requests.get(1).getId(),2L);
    }

    @Test
    @DirtiesContext
    public void testFindALlByAuthorInOrderByCreatedAsc() {
        User user1 = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user1);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        ItemRequest request1 = new ItemRequest("itemReq1",user1, LocalDateTime.now());
        itemRequestRepository.save(request1);

        ItemRequest request2 = new ItemRequest("itemReq2",user2, LocalDateTime.now().minusDays(1));
        itemRequestRepository.save(request2);

        ItemRequest request3 = new ItemRequest("itemReq3",user2, LocalDateTime.now().minusDays(2));
        itemRequestRepository.save(request3);

        List<ItemRequest> requests = itemRequestRepository.findALlByAuthorInOrderByCreatedAsc(List.of(user1,user2), Pageable.unpaged()).getContent();
        assertEquals(requests.size(),3);
        assertEquals(requests.get(0).getId(),3L);
        assertEquals(requests.get(1).getId(),2L);
        assertEquals(requests.get(2).getId(),1L);
    }
}