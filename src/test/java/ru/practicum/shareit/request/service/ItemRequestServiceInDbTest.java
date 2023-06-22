package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;
import ru.practicum.shareit.user.service.UserServiceInDb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceInDbTest {

    private ItemRequestServiceInDb itemRequestService;

    @Mock
    private UserServiceInDb userService;

    @Mock
    private ItemRequestRepositoryInDb itemRequestRepository;

    @Mock
    private UserRepositoryInDb userRepository;

    @BeforeEach
    public void setUp() {
        itemRequestService = new ItemRequestServiceInDb(userService,itemRequestRepository,userRepository);
    }

    @Test
    void newItemRequestAndGetTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description1")
                .created(LocalDateTime.now())
                .items(List.of(ItemDto.builder().build()))
                .build();

        User user = new User(1L,"name","email@mail.com");

        when(userService.get(anyLong()))
                .thenReturn(user);

        itemRequestService.newItemRequest(1L,itemRequestDto);
        verify(itemRequestRepository,times(1)).save(any(ItemRequest.class));

        when(itemRequestRepository.findByAuthor_IdOrderByCreatedAsc(anyLong()))
                .thenReturn(List.of(ItemRequestMapper.fromDto(itemRequestDto,user)));

        List<ItemRequest> requests = itemRequestService.getMyItemRequest(1L);
        assertEquals(requests.size(),1);
        assertEquals(requests.get(0).getDescription(),"description1");
        verify(itemRequestRepository,times(1)).findByAuthor_IdOrderByCreatedAsc(anyLong());

        assertThrows(NullPointerException.class, ()-> itemRequestService.getAllItemRequest(null,null,null));
        assertThrows(NullPointerException.class, ()-> itemRequestService.getAllItemRequest(0L,null,null));

        when(userRepository.findByIdNot(anyLong()))
                .thenReturn(List.of(user));

        when(itemRequestRepository.findALlByAuthorInOrderByCreatedAsc(anyList(),any(Pageable.class)))
                .thenReturn(Page.empty());

        List<ItemRequest> requestsAllIR = itemRequestService.getAllItemRequest(1L,null,null);
        assertTrue(requestsAllIR.isEmpty());
        verify(userRepository,times(1)).findByIdNot(anyLong());
        verify(itemRequestRepository,times(1)).findALlByAuthorInOrderByCreatedAsc(anyList(),any(Pageable.class));

        assertThrows(NullPointerException.class, ()-> itemRequestService.getItemRequest(1L,null));
        assertThrows(NullPointerException.class, ()-> itemRequestService.getItemRequest(1L,0L));

        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, ()-> itemRequestService.getItemRequest(1L,1L));

        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(ItemRequestMapper.fromDto(itemRequestDto,user)));
        ItemRequest itemRequest = itemRequestService.getItemRequest(1L,1L);
        assertNotNull(itemRequest);
        verify(itemRequestRepository,times(2)).findById(anyLong());
    }
}