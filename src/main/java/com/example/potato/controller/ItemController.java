package com.example.potato.controller;

import com.example.potato.dto.PurchaseRequest;
import com.example.potato.entity.Inventory;
import com.example.potato.entity.Item;
import com.example.potato.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Item (상점)", description = "아이템 조회 및 구매 API")
public class ItemController {

    private final ItemService itemService;

    // 1. 전체 아이템 목록 조회
    @GetMapping
    @Operation(summary = "아이템 목록 조회", description = "상점에서 판매 중인 모든 아이템 목록을 반환합니다.")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // 2. 아이템 상세 조회
    @GetMapping("/{id}")
    @Operation(summary = "아이템 상세 조회", description = "특정 ID를 가진 아이템의 상세 정보를 조회합니다.")
    public ResponseEntity<Item> getItemById(@PathVariable(name = "id") Long id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    // 3. 카테고리별 아이템 조회
    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 아이템 조회", description = "카테고리별로 아이템을 필터링하여 조회합니다.")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable(name = "category") String category) {
        List<Item> items = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    // 4. 아이템 구매
    @PostMapping("/purchase")
    @Operation(summary = "아이템 구매", description = "유저 ID와 아이템 ID를 JSON으로 받아 구매를 처리합니다.")
    public ResponseEntity<String> purchaseItem(@RequestBody PurchaseRequest request) {
        try {
            // ItemService에 작성한 구매 로직 호출
            String result = itemService.purchaseItem(request);

            if (result.contains("완료")) {
                return ResponseEntity.ok(result);
            } else {
                // 잔액 부족이나 중복 구매의 경우 400 에러 반환
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            // 서버 내부 로직 에러 발생 시 500 에러 반환
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 5.아이템 조회
    @GetMapping("/inventory/{userId}")
    @Operation(summary = "내 인벤토리 조회", description = "특정 유저가 보유한 아이템 목록을 조회합니다.")
    public ResponseEntity<List<Inventory>> getUserInventory(@PathVariable(name = "userId") Long userId) {
        List<Inventory> inventory = itemService.getUserInventory(userId);
        return ResponseEntity.ok(inventory);
    }

    // 6. 아이템 장착
    @PostMapping("/inventory/equip")
    @Operation(summary = "아이템 장착", description = "인벤토리 ID를 이용해 아이템을 장착합니다. 같은 카테고리의 기존 아이템은 자동으로 해제됩니다.")
    public ResponseEntity<String> equipItem(@RequestParam(name = "userId") Long userId,
            @RequestParam(name = "inventoryId") Long inventoryId) {
        try {

            String result = itemService.equipItem(userId, inventoryId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 장착 실패 시(본인 아이템이 아니거나 아이템이 없을 때) 에러 메시지 반환
            return ResponseEntity.badRequest().body("장착 실패: " + e.getMessage());
        }
    }
}