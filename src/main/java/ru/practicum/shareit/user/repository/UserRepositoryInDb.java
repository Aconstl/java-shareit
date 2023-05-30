
package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

public interface UserRepositoryInDb extends JpaRepository<User,Long> {
    @Query (value = "UPDATE PUBLIC.USERS SET USERNAME = ?2, email = ?3 " +
            "WHERE user_id = ?1", nativeQuery = true)
    User updateUser(Integer id, String username, String email);
}


