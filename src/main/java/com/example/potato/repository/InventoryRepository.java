package com.example.potato.repository;

import com.example.potato.entity.Inventory;
import com.example.potato.entity.Item;
import com.example.potato.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 상점 아이템 목록
    List<Inventory> findByUser_Id(Long userId);

    // 이미 산 아이템인지 확인(구매 서비스)
    boolean existsByUserAndItem(User user, Item item);

    // 유저 ID로 인벤토리 목록 조회 (아이템 정보까지 같이 가져오기)
    List<Inventory> findByUserId(Long userId);
}