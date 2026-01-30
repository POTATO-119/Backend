package com.example.potato.controller;

import com.example.potato.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@Tag(name = "Shop (상점 및 구매)", description = "팀원 B 담당: 아이템 구매 API")
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/purchase")
    @Operation(summary = "아이템 구매하기", description = "유저 ID와 아이템 ID를 받아 구매를 진행합니다.")
    public ResponseEntity<String> purchaseItem(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "itemId") Long itemId) {

        shopService.purchaseItem(userId, itemId);
        return ResponseEntity.ok("구매 성공! 인벤토리에 추가되었습니다.");
    }
}