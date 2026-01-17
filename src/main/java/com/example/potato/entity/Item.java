package com.example.potato.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    private Integer price;

    @Column(name = "is_limited")
    private Boolean isLimited;
}
