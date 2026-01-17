package com.example.potato.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_character")
@Getter
@Setter
@NoArgsConstructor
public class UserCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Long id;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "head_item_id")
    private Item headItem;

    @ManyToOne
    @JoinColumn(name = "body_item_id")
    private Item bodyItem;

    @ManyToOne
    @JoinColumn(name = "acc_item_id")
    private Item accItem;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
