package com.example.potato.service;

import com.example.potato.dto.PurchaseRequest;
import com.example.potato.entity.Inventory;
import com.example.potato.entity.User;
import com.example.potato.entity.Item;
import com.example.potato.repository.InventoryRepository;
import com.example.potato.repository.ItemRepository;
import com.example.potato.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    // 전체 아이템 목록 조회
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // 아이템 상세 조회
    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다. id=" + id));
    }

    // 카테고리별 아이템 조회
    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }

    @Transactional
    public String purchaseItem(PurchaseRequest request) {

        // 1. 유저와 아이템 정보 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 2. 중복 구매 체크
        if (inventoryRepository.existsByUserAndItem(user, item)) {
            return "이미 보유 중인 아이템입니다.";
        }

        // 3. 잔액(스푼) 검증
        if (user.getSpoon() < item.getPrice()) {
            return "스푼이 부족합니다. (현재: " + user.getSpoon() + ")";
        }

        // 4. 스푼 차감
        user.setSpoon(user.getSpoon() - item.getPrice());
        userRepository.save(user);

        // 5. 인벤토리에 추가 (장착 상태는 기본 false)
        Inventory inventory = new Inventory(user, item);
        inventoryRepository.save(inventory);

        return "구매가 완료되었습니다! 잔여 스푼: " + user.getSpoon();
    }

    public List<Inventory> getUserInventory(Long userId) {
        // 유저 존재 여부 확인 후 인벤토리 반환
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
        return inventoryRepository.findByUserId(userId);
    }

    @Transactional
    public String equipItem(Long userId, Long inventoryId) {

        // 1. 해당 인벤토리 아이템 찾기
        Inventory target = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다."));

        // 2. 본인의 아이템 검증
        if (!target.getUser().getId().equals(userId)) {
            return "본인의 아이템만 장착할 수 있습니다.";
        }

        // 3.같은 카테고리의 기존 장착 아이템 해제
        String category = target.getItem().getCategory();
        List<Inventory> myInventory = inventoryRepository.findByUserId(userId);

        for (Inventory item : myInventory) {
            if (item.getItem().getCategory().equals(category) && item.isEquipped()) {
                item.setEquipped(false); // 기존 같은 카테고리 아이템 벗기
            }
        }

        // 4. 선택한 아이템 장착
        target.setEquipped(true);
        inventoryRepository.save(target);

        return target.getItem().getName() + " 아이템을 장착했습니다.";
    }
}