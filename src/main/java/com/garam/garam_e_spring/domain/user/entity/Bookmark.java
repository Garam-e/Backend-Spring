package com.garam.garam_e_spring.domain.user.entity;

import com.garam.garam_e_spring.global.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@SuperBuilder
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @Column
    private String message;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    public static Bookmark createBookmark(String message, User user) {
        Bookmark bookmark = Bookmark.builder()
                .message(message)
                .user(user)
                .build();

        user.addBookmark(bookmark);
        return bookmark;
    }
}
