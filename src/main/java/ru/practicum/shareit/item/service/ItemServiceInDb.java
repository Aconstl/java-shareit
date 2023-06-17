package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentDtoIn;
import ru.practicum.shareit.item.comment.model.CommentDtoOut;
import ru.practicum.shareit.item.comment.model.CommentMapper;
import ru.practicum.shareit.item.comment.repository.CommentRepositoryInDb;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceInDb;


import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("ItemServiceInDb")
@Primary
public class ItemServiceInDb implements ItemService {

    private final ItemRepositoryInDb itemRepository;
    private final UserServiceInDb userService;

    private final BookingRepositoryInDb bookingRepository;
    private final CommentRepositoryInDb commentRepository;

    private final ItemRequestServiceInDb itemRequestService;
    /*
    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public Item create(Long userId, ItemDto itemDto) {
        log.trace("добавление предмета");
        User user = userService.get(userId);
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user);
        return itemRepository.save(item);
    }
*/
    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public Item create(Long userId, ItemDto itemDto) {
        log.trace("добавление предмета");
        User user = userService.get(userId);
        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestService.getItemRequest(userId, itemDto.getRequestId());
            item.setRequest(itemRequest);
        }
        return itemRepository.save(item);
    }

    public Item find(Long id) {
        if (id == null || id == 0) {
            throw new NullPointerException("Id предмета указан неверно");
        }
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Предмет с Id № " + id + " не найден");
        }
        return item.get();
    }

    @Override
    public ItemDtoWithBooking get(Long id,Long userId) {
        log.trace("получение предмета");
        Item item = find(id);
        ItemDtoWithBooking itemFin = addBookingAndComment(item,userId);
        log.debug("Предмет с id №{} получен", id);
        return itemFin;
    }

    @Override
    public List<ItemDtoWithBooking> getAllItemUsers(Long userId) {
        log.trace("вывод всех предметов пользователя");
        Pageable pageable = Pageable.unpaged();

        @SuppressWarnings("unchecked")
        Page<Item> itemPage = itemRepository.findByOwner_IdOrderByIdAsc(userId, pageable);

        List<Item> items = itemPage.getContent();
        // Выгружаем комментарии, группируем их по вещам и конвертируем в dto
        List<Comment> commentList =  commentRepository.findAllByItemIn(items);
        Map<Item, List<Comment>> comments = commentList.stream().collect(groupingBy(Comment::getItem, toList()));

        // Выгружаем подтвержденные бронирования отсортированные по дате начала
        // и группируем их по вещам
        List<Booking> bookingList = bookingRepository.findAllByStatusAndItemIn(Status.APPROVED,items);
        Map<Item, List<Booking>> bookings = bookingList.stream().collect(groupingBy(Booking::getItem, toList()));

        // Для каждой вещи находим последнее и следующее бронирование, а также комментарии
        // и формируем результат
        List<ItemDtoWithBooking> results = new ArrayList<>();

        for (Item item : items) {

            List<Booking> bookingItem = bookings.get(item);
            Booking lastBooking = null;
            Booking nextBooking = null;
            //Поиск последнего
            if (bookingItem !=null && !bookingItem.isEmpty()) {
                for (Booking b : bookingItem) {
                    if (b.getStart().isBefore(LocalDateTime.now())) {
                        if (lastBooking == null || lastBooking.getStart().isBefore(b.getStart())) {
                            lastBooking = b;
                        }
                    }
                }

                //Поиск следующего
                if (lastBooking != null) {
                    for (Booking b : bookingItem) {
                        if (lastBooking.getStart().isBefore(b.getStart())) {
                            if (nextBooking == null || nextBooking.getStart().isAfter(b.getStart())) {
                                nextBooking = b;
                            }
                        }
                    }
                }
            }
            //Поиск комментов
            List<Comment> commentsItem = new ArrayList<>();
            if (comments != null && !comments.isEmpty()) {
                commentsItem = comments.get(item);
            }
            //Преобразование в нужный формат
            ItemDtoWithBooking dto = ItemMapper.toDtoWithBooking(item,
                    BookingMapper.toDtoForItem(lastBooking),
                    BookingMapper.toDtoForItem(nextBooking),
                    CommentMapper.toListDto(commentsItem));
            results.add(dto);
        }

        return results;
    }

    @Override
    @Transactional
    public List<Item> search(String text) {
        log.trace("поиск предмета по имени");
        if (!text.isBlank()) {
            return itemRepository.searchItem(text.toLowerCase());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        log.trace("обновление предмета");
        if (itemId == null || itemId == 0) {
            throw new ValidationException("предмет имеет ошибочное id");
        } else {
            if (!itemRepository.getOwnerId(itemId).equals(userId)) {
                throw new IllegalArgumentException("пользователь не является собственником указанного предмета");
           } else {
                String name = itemDto.getName();
                String description = itemDto.getDescription();
                Boolean available = itemDto.getAvailable();
                if (itemDto.getName() != null) {
                    itemRepository.updateName(itemId,name);
                }
                if (itemDto.getDescription() != null) {
                    itemRepository.updateDescription(itemId, description);
                }
                if (itemDto.getAvailable() != null) {
                    itemRepository.updateAvailable(itemId, available);
                }
            }
        }
        return find(itemId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.trace("удаление предмета");
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Comment postComment(Long userId, Long itemId, CommentDtoIn commentDtoIn) {
        log.trace("Добавление комментария к предмету");
        //бронировал ли пользователь предмет чтобы оставлять отзыв?
        Item item = itemRepository.findBookingUser(userId,itemId);
        if (item == null) {
            throw new ValidationException("Данный пользователь не бронировал предмет");
        }
        User user = userService.get(userId);
        Comment comment = CommentMapper.fromDto(commentDtoIn,item,user, LocalDateTime.now());
        return commentRepository.save(comment);
    }

    private ItemDtoWithBooking addBookingAndComment(Item item,Long userId) {
        Booking lastBooking = null;
        Booking nextBooking = null;

        if (userId.equals(item.getOwner().getId())) {
            lastBooking = bookingRepository.getLastBooking(item.getId());
            if (lastBooking == null || lastBooking.getBooker().getId().equals(item.getOwner().getId())) {
                lastBooking = null;
            } else {
                nextBooking = bookingRepository.getNextBooking(item.getId(), lastBooking.getEnd());
            }
        }
        List<CommentDtoOut> comments = CommentMapper.toListDto(commentRepository.findAllByItemId(item.getId()));
        return ItemMapper.toDtoWithBooking(item,
                BookingMapper.toDtoForItem(lastBooking),
                BookingMapper.toDtoForItem(nextBooking),
                comments);
    }

}
