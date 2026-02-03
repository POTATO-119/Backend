package com.example.potato.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    @Column(name = "acquired_at")
    private LocalDateTime acquiredAt;

    @Column(name = "is_equipped")
    private boolean isEquipped = false;// 기본값은 미장착

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 구매 로직에서 사용
    public Inventory(User user, Item item) {
        this.user = user;
        this.item = item;
        this.acquiredAt = LocalDateTime.now();
        this.isEquipped = false;
    }
}