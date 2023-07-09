package ru.practicum.shareit.item.comment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryInDb;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryInDb;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class CommentRepositoryInDbTest {

    @Autowired
    UserRepositoryInDb userRepository;

    @Autowired
    ItemRepositoryInDb itemRepository;

    @Autowired
    CommentRepositoryInDb commentRepository;

    @Test
    @DirtiesContext
    public void testFindAllByItemIn() {
        User user1 = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user1);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        User user3 = new User(3L,"user3","user3@mail.ru");
        userRepository.save(user3);

        Item item1 = new Item(1L,"item1","itemDecription1",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        Item item2 = new Item(2L,"item2","itemDecription2",true);
        item2.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item2);

        LocalDateTime date = LocalDateTime.now();
        Comment comment1 = new Comment("user2 for item1", item1,  user2, date);
        commentRepository.save(comment1);

        Comment comment2 = new Comment("user3 for item2", item2,  user3, date);
        commentRepository.save(comment2);

        Comment comment3 = new Comment("user3 for item1", item1,  user3, date);
        commentRepository.save(comment3);

        Comment comment4 = new Comment("user2 for item2", item2,  user2, date);
        commentRepository.save(comment4);

        List<Comment> comments = commentRepository.findAllByItemIn(List.of(item1,item2));
        assertEquals(comments.size(),4);
        assertEquals(comments.get(0).getText(),"user2 for item1");
        assertEquals(comments.get(1).getText(),"user3 for item1");
        assertEquals(comments.get(2).getText(),"user3 for item2");
        assertEquals(comments.get(3).getText(),"user2 for item2");
    }

    @Test
    @DirtiesContext
    public void testFindAllByItemId() {
        User user1 = new User(1L,"user1","user1@mail.ru");
        userRepository.save(user1);

        User user2 = new User(2L,"user2","user2@mail.ru");
        userRepository.save(user2);

        User user3 = new User(3L,"user3","user3@mail.ru");
        userRepository.save(user3);

        User user4 = new User(4L,"user4","user4@mail.ru");
        userRepository.save(user4);

        Item item1 = new Item(1L,"item1","itemDecription1",true);
        item1.setOwner(userRepository.findById(1L).get());
        itemRepository.save(item1);

        LocalDateTime date = LocalDateTime.now();
        Comment comment1 = new Comment("user3 for item1", item1,  user3, date);
        commentRepository.save(comment1);

        Comment comment2 = new Comment("user4 for item1", item1,  user4, date);
        commentRepository.save(comment2);

        Comment comment3 = new Comment("user2 for item1", item1,  user2, date);
        commentRepository.save(comment3);

        List<Comment> comments = commentRepository.findAllByItemId(1L);
        assertEquals(comments.size(),3);
        assertEquals(comments.get(0).getText(),"user3 for item1");
        assertEquals(comments.get(1).getText(),"user4 for item1");
        assertEquals(comments.get(2).getText(),"user2 for item1");
    }
}