package com.example.potato.service;

import com.example.potato.entity.Inventory;
import com.example.potato.entity.Item;
import com.example.potato.entity.User;
import com.example.potato.repository.InventoryRepository;
import com.example.potato.repository.ItemRepository;
import com.example.potato.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void purchaseItem(Long userId, Long itemId) {
        // 1. 유저 및 아이템 정보 pull
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템입니다."));

        // 2. 스푼 잔액 검증_디자인 시안 기반
        if (user.getSpoonAmount() < item.getPrice()) {
            throw new RuntimeException("스푼이 부족합니다! 현재 잔액: " + user.getSpoonAmount());
        }

        // 3. 포인트(스푼)차감
        user.setSpoonAmount(user.getSpoonAmount() - item.getPrice());

        // 4. 인벤토리 추가_구매 이력 저장
        // Inventory 엔티티에 User와 Item을 받는 user

        Inventory inventory = new Inventory();
        inventory.setUser(user);
        inventory.setItem(item);

        inventoryRepository.save(inventory);
    }
}