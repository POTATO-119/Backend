package com.example.potato.service;

import com.example.potato.entity.Item;
import com.example.potato.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

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
}