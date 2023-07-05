package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepositoryInDb extends JpaRepository<ItemRequest,Long> {

    List<ItemRequest> findByAuthor_IdOrderByCreatedAsc(Long userId);

    Page<ItemRequest> findALlByAuthorInOrderByCreatedAsc(List<User> users, Pageable pageable);
}
