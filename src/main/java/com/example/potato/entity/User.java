package com.example.potato.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    private String password;

    @Column(name = "current_xp")
    private Integer currentXp;

    @Column(name = "level_step")
    private Integer levelStep;

    @Column(name = "spoon_amount")
    private Integer spoonAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public int getSpoon() {
        return this.spoonAmount != null ? this.spoonAmount : 0;
    }

    public void setSpoon(int spoon) {
        this.spoonAmount = spoon;
    }
}
