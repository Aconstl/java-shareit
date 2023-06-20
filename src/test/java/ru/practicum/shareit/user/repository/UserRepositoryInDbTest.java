package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryInDbTest {
    @Autowired
    UserRepositoryInDb userRepository;

    @Test
    public void testFindByIdNot() {
        userRepository.save(new User(1L,"user1","user1@mail.ru"));
        userRepository.save(new User(2L,"user2","user2@mail.ru"));
        userRepository.save(new User(3L,"user3","user3@mail.ru"));

        List<User> userList = userRepository.findByIdNot(2L);
        assertEquals(userList.size(),2);
        List<User> u = userList.stream()
                .filter(user -> user.getId() == 2)
                .collect(Collectors.toList());
        assertEquals(u.size(),0);
    }


    @Test
    @Commit
    public void testUpdateName() {
        userRepository.save(new User(1L,"user1","user1@mail.com"));

        userRepository.updateUsername(1L,"user1Update");
        assertEquals(userRepository.findById(1L).get().getName(),"user1Update");
    }

    @Test
    @Commit
    public void testUpdateEmail() {
        userRepository.save(new User(1L, "user1","user1@mail.com"));

        userRepository.updateUserEmail(1L, "user1Update@mail.com");
        assertEquals(userRepository.findById(1L).get().getEmail(),"user1Update@mail.com" );
    }

}