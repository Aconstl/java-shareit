
package ru.practicum.shareit.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepositoryInDb extends JpaRepository<User,Long> {

 //   Page findALlByAuthor_IdNotOrderByCreatedAsc(Long userId, Pageable pageable);

    List<User> findByIdNot(Long userId);

    @Modifying
    @Query (value = "UPDATE PUBLIC.USERS " +
            "SET username = :username " +
            "WHERE user_id = :user_id", nativeQuery = true)
    void updateUsername(@Param("user_id") Long id,
                    @Param("username") String username
                   );

    @Modifying
    @Query (value = "UPDATE PUBLIC.USERS " +
            "SET email = :email " +
            "WHERE user_id = :user_id", nativeQuery = true)
    void updateUserEmail(@Param("user_id") Long id,
                    @Param("email") String email
    );
}


