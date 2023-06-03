package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryInDb extends JpaRepository<Item,Long> {

    @Query (value = "select * " +
            "from public.items " +
            "where user_id = :id "
            , nativeQuery = true)
    List<Item> findItemByOwner(@Param("id") Long id);

    @Query (value = "select * " +
            "from public.items " +
            "where available = true AND " +
            "( LOWER(name) LIKE %?1% OR LOWER(description) LIKE %?1% ) "
            , nativeQuery = true)
    List<Item> searchItem(String text);

    @Query (value = "select user_id " +
            "from public.items " +
            "where item_id = ?1", nativeQuery = true)
    Long getOwnerId(Long id);

    @Modifying
    @Query (value = "UPDATE PUBLIC.ITEMS " +
            "SET NAME = :name " +
            "WHERE item_id = :item_id", nativeQuery = true)
    void updateName(@Param("item_id") Long id,
                    @Param("name") String name
                    );

    @Modifying
    @Query (value = "UPDATE PUBLIC.ITEMS " +
            "SET description = :description " +
            "WHERE item_id = :item_id", nativeQuery = true)
    void updateDescription(@Param("item_id") Long id,
                           @Param("description") String description
                            );

    @Modifying
    @Query (value = "UPDATE PUBLIC.ITEMS " +
            "SET available = :available " +
            "WHERE item_id = :item_id", nativeQuery = true)
    void updateAvailable(@Param("item_id") Long id,
                         @Param("available") Boolean available
                        );
}