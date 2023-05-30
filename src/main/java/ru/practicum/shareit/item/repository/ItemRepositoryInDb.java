package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryInDb extends JpaRepository<Item,Long> {
    List<Item> findByNameContainingIgnoreCase(String text);

    //подумать над прописанием логики изменения (надо в where еще добавить owner_id или подобное)
    @Query(value = "UPDATE PUBLIC.ITEMS SET NAME = ?2, description = ?3, AVAILABLE = ?4 " +
            "WHERE item_id = ?1", nativeQuery = true)
    Item updateItem(Integer id, String name, String description,boolean available);
}
