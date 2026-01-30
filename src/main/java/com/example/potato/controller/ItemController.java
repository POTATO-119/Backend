package com.example.potato.controller;

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
@Tag(name = "Item (상점)", description = "아이템 조회 및 상점 관리 API")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @Operation(summary = "아이템 목록 조회", description = "상점에서 판매 중인 모든 아이템 목록을 반환합니다.")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @Operation(summary = "아이템 상세 조회", description = "특정 ID를 가진 아이템의 상세 정보를 조회합니다.")
    public ResponseEntity<Item> getItemById(@PathVariable(name = "id") Long id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 아이템 조회", description = "HEADWEAR, OUTFIT, ACCESSORY 등 카테고리별로 필터링합니다.")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable(name = "category") String category) {
        List<Item> items = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }
}