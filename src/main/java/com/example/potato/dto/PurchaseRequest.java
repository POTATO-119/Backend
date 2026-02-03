package com.example.potato.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {
    private Long userId; // 구매하는 유저 ID
    private Long itemId; // 구매할 아이템 ID
}