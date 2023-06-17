package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
@Getter
@Setter
public class ItemRequest {

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User author;

    @NotNull
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    @PastOrPresent
    private LocalDateTime created;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private List<Item> items;

    public ItemRequest(String description,User author,LocalDateTime created) {
        this.description = description;
        this.author = author;
        this.created = created;
    }
}
