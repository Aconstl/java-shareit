package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;
import ru.practicum.shareit.user.service.UserServiceInDb;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("ItemRequestServiceInDb")
@Primary
public class ItemRequestServiceInDb implements ItemRequestService {

    private final UserServiceInDb userService;
    private final ItemRequestRepositoryInDb itemRequestRepository;
    private final ItemRepositoryInDb itemRepository;
    private final UserRepositoryInDb userRepository;

    @Override
    @Transactional
    public ItemRequest newItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        log.trace("создание запроса на предмет");
        User user = userService.get(userId);
        ItemRequest itemRequest = ItemRequestMapper.fromDto(itemRequestDto,user);
        return itemRequestRepository.save(itemRequest);
    }

    public List<ItemRequest> getMyItemRequest(Long userId) {
        log.trace("получение списка запросов пользователя");
        User user = userService.get(userId);
        List<ItemRequest> listItemRequest = itemRequestRepository.findByAuthor_IdOrderByCreatedAsc(user.getId());
        return listItemRequest;
    }

    public List<ItemRequest> getAllItemRequest(Long userId, Long from, Long size) {
        log.trace("получение списка запросов других пользователей");

    //    Page<User> users = userRepository.findALlByAuthor_IdNotOrderByCreatedAsc(userId,pageable);
        if (userId == null || userId == 0) {
            throw new NullPointerException("Id пользователя указано неверно");
        }

        List<User> users = userRepository.findByIdNot(userId);

        Pageable pageable = Pagination.setPageable(from,size);

        Page<ItemRequest> itemReq = itemRequestRepository.findALlByAuthorInOrderByCreatedAsc(users,pageable);

        List<ItemRequest> itemRequests = itemReq.getContent();

        return itemRequests;
    }

    public ItemRequest getItemRequest(Long userId,Long itemRequestId) {
        log.trace("получение данных о конкретном запросе");
        User user = userService.get(userId);
        if (itemRequestId == null || itemRequestId == 0) {
            throw new NullPointerException("Id запроса указано неверно");
        }
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(itemRequestId);
        if (itemRequest.isEmpty()) {
            throw new IllegalArgumentException("Запрос с Id № " + itemRequestId + " не найден");
        }
        ItemRequest request = itemRequest.get();
        log.debug("Пользователь с id №{} получен", itemRequestId);

        return request;
    }

}
