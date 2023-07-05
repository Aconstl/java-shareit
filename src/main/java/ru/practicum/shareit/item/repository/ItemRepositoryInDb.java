package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepositoryInDb extends JpaRepository<Item,Long> {

    Page<Item> findByOwner_IdOrderByIdAsc(Long userId, Pageable pageable);

    @Query (value = "select * " +
            "from public.items " +
            "where available = true AND " +
            "( LOWER(name) LIKE %?1% OR LOWER(description) LIKE %?1% ) ", nativeQuery = true)
    Page<Item> searchItem(String text,Pageable pageable);

    @Query (value = "select user_id " +
            "from public.items " +
            "where item_id = ?1", nativeQuery = true)
    Long getOwnerId(Long id);

    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query (value = "UPDATE PUBLIC.ITEMS " +
            "SET NAME = :name " +
            "WHERE item_id = :item_id", nativeQuery = true)
    void updateName(@Param("item_id") Long id,
                    @Param("name") String name
                    );

    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query (value = "UPDATE PUBLIC.ITEMS " +
            "SET description = :description " +
            "WHERE item_id = :item_id", nativeQuery = true)
    void updateDescription(@Param("item_id") Long id,
                           @Param("description") String description
                            );

    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query (value = "UPDATE PUBLIC.ITEMS " +
            "SET available = :available " +
            "WHERE item_id = :item_id", nativeQuery = true)
    void updateAvailable(@Param("item_id") Long id,
                         @Param("available") Boolean available
                        );

    @Query(value = "select distinct i.* " +
            "From bookings b " +
            "left join items i on i.item_id = b.item_id " +
            "Where b.booker_id = :userId " +
            "AND i.item_id = :itemId " +
            "and b.end_date < now() " +
            "AND not b.status = 'REJECTED' ", nativeQuery = true)
    Item findBookingUser(@Param("userId") Long userId,
                         @Param("itemId") Long itemId);

}
