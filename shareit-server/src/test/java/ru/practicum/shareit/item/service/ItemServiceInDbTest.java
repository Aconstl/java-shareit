package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepositoryInDb;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.model.CommentDtoIn;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class ItemServiceInDbTest {

    private ItemService itemService;

    @Mock
    ItemRepositoryInDb itemRepository;

    @Mock
    UserServiceInDb userService;

    @Mock
    BookingRepositoryInDb bookingRepository;

    @Mock
    CommentRepositoryInDb commentRepository;

    @Mock
    ItemRequestServiceInDb itemRequestService;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceInDb(itemRepository,userService,bookingRepository,commentRepository,
                itemRequestService);
    }

    @Test
    public void createAndGetItemTest() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("descriptionItem")
                .available(true)
                .build();

        User user = new User(1L,"user1","user1@mail.ru");

        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user);

        when(userService.get(user.getId()))
                .thenReturn(user);
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(bookingRepository.getLastBooking(itemDto.getId()))
                .thenReturn(null);
        when(commentRepository.findAllByItemId(itemDto.getId()))
                .thenReturn(new ArrayList<>());

        itemService.create(user.getId(),itemDto);
    // добавить сразу логику на бронирование, комментарии
        ItemDtoWithBooking itemRes = itemService.get(1L,1L);
        assertEquals(itemRes.getId(),1L);
        assertNull(itemRes.getLastBooking());
        assertNull(itemRes.getNextBooking());
        assertTrue(itemRes.getComments().isEmpty());
    }

    @Test
    public void getItemFailTest() {
        assertThrows(NullPointerException.class, () -> itemService.get(null,1L));
        assertThrows(NullPointerException.class, () -> itemService.get(0L,1L));
        assertThrows(IllegalArgumentException.class, () -> itemService.get(1L,1L));
    }

    @Test
    public void createAndGetItemWithAllParametersTest() {
        User user1 = new User(1L,"user1","user1@mail.ru");
        User user2 = new User(2L,"user2","user2@mail.ru");
        User user3 = new User(3L,"user3","user3@mail.ru");

        ItemRequest request = new ItemRequest("need item",user1,LocalDateTime.now());

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("descriptionItem")
                .available(true)
                .requestId(1L)
                .build();

        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user2);

        when(userService.get(user2.getId()))
                .thenReturn(user2);

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        when(itemRequestService.getItemRequest(user2.getId(),itemDto.getRequestId()))
                .thenReturn(request);

        when(bookingRepository.getLastBooking(itemDto.getId()))
                .thenReturn(new Booking(item,user3,LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1), Status.APPROVED));

        when(bookingRepository.getNextBooking(anyLong(),any(LocalDateTime.class)))
                .thenReturn(new Booking(item,user3,LocalDateTime.now().plusDays(1),
                      LocalDateTime.now().plusDays(2), Status.WAITING));

        when(commentRepository.findAllByItemId(itemDto.getId()))
                .thenReturn(List.of(new Comment("comment",item,user3,LocalDateTime.now())));

        itemService.create(user2.getId(),itemDto);


        ItemDtoWithBooking itemRes = itemService.get(1L,2L);
        assertEquals(itemRes.getId(),1L);
        assertNotNull(itemRes.getLastBooking());
        assertNotNull(itemRes.getNextBooking());
        assertFalse(itemRes.getComments().isEmpty());
    }

    @Test
    public void updateAndSearchAndDeleteItemTest() {
        User user1 = new User(1L,"user1","user1@mail.ru");

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("descriptionItem")
                .available(true)
                .build();

        ItemDto itemDtoUpd = ItemDto.builder()
                .id(1L)
                .name("itemUpd")
                .description("descriptionItemUpd")
                .available(false)
                .build();

        itemService.create(user1.getId(),itemDto);

        when(itemRepository.getOwnerId(itemDto.getId()))
                .thenReturn(1L);

        //Неудачные обновления
        assertThrows(IllegalArgumentException.class, () -> itemService.update(2L,itemDto.getId(),itemDtoUpd));

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(ItemMapper.fromDto(itemDtoUpd)));

        itemService.update(1L,itemDto.getId(),itemDtoUpd);
        verify(itemRepository,times(1)).updateName(anyLong(),anyString());
        verify(itemRepository,times(1)).updateDescription(anyLong(),anyString());
        verify(itemRepository,times(1)).updateAvailable(anyLong(),anyBoolean());


        //Поиск по тексту
        Page<Item> itemSearch = new PageImpl<>(List.of(ItemMapper.fromDto(itemDtoUpd)),Pageable.unpaged(),1L);
        when(itemRepository.searchItem("itemupd", Pageable.unpaged()))
                .thenReturn(itemSearch);

        List<Item> searchRes = itemService.search("itemupd",null,null);
        assertEquals(searchRes.size(),1);
        itemService.delete(1L);
        verify(itemRepository,times(1)).deleteById(1L);

    }

    @Test
    public void commentTest() {
        CommentDtoIn comment = new CommentDtoIn();
        comment.setText("text");

        Long userId = 1L;
        Long itemId = 1L;

        when(itemRepository.findBookingUser(anyLong(), anyLong()))
                .thenReturn(null);

        assertThrows(ValidationException.class, () -> itemService.postComment(2L,2L,comment));

        when(itemRepository.findBookingUser(userId, itemId))
                .thenReturn(new Item());
        when(userService.get(userId))
                .thenReturn(new User());
        itemService.postComment(userId,itemId,comment);
        verify(itemRepository,times(2)).findBookingUser(anyLong(),anyLong());
        verify(userService,times(1)).get(anyLong());
    }

    @Test
    public void getAllItemUsersTest() {
        User user1 = new User(1L,"user1","user1@mail.ru");
        User user2 = new User(2L,"user2","user2@mail.ru");

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("descriptionItem")
                .available(true)
                .requestId(1L)
                .build();

        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(user1);

        Page<Item> itemSearch = new PageImpl<>(List.of(item),Pageable.unpaged(),1L);
        when(itemRepository.findByOwner_IdOrderByIdAsc(user1.getId(),Pageable.unpaged()))
                .thenReturn(itemSearch);

        when(commentRepository.findAllByItemIn(anyList()))
                .thenReturn(List.of(new Comment("",item,user2,LocalDateTime.now())));
        when(bookingRepository.findAllByStatusAndItemInOrderByStartAsc(Status.APPROVED,List.of(item)))
                .thenReturn(List.of(
                        new Booking(item,user2,LocalDateTime.now().minusDays(2),
                            LocalDateTime.now().minusDays(1), Status.APPROVED),
                        new Booking(item,user2,LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().plusDays(2), Status.WAITING)
                        ));
        List<ItemDtoWithBooking> result = itemService.getAllItemUsers(user1.getId(),null,null);
        assertEquals(result.size(),1);
    }

}