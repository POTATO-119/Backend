package com.example.potato.repository;

import com.example.potato.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 상점 아이템 목록
    List<Inventory> findByUser_Id(Long userId);
}