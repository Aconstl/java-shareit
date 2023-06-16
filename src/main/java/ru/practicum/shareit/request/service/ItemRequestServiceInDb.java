package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.item.service.ItemServiceInDb;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceInDb;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("ItemRequestServiceInDb")
@Primary
public class ItemRequestServiceInDb implements ItemRequestService {

    private final UserServiceInDb userService;
    private final ItemServiceInDb itemService;
    private final ItemRequestRepositoryInDb itemRequestRepository;
    private final ItemRepositoryInDb itemRepository;

    @Override
    @Transactional
    public ItemRequest newItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        log.trace("создание запроса на предмет");
        User user = userService.get(userId);
        ItemRequest itemRequest = ItemRequestMapper.fromDto(itemRequestDto,user);
        return itemRequestRepository.save(itemRequest);
    }

    public List<ItemRequest> getMyItemRequest(Long userId) {
        log.trace("получение списка запросов юзера");
        User user = userService.get(userId);
        //Надо подумать над реализацией - восстановить логику, представленную ревьюером

    }

    public List<ItemRequest> getAllItemRequest(Long userId, Long from, Long size) {
        throw new UnsupportedOperationException("");
    }

    public ItemRequest getItemRequest(Long itemRequestId) {
        throw new UnsupportedOperationException("");
    }

}
