package com.example.potato.service;

import com.example.potato.dto.PurchaseRequest;
import com.example.potato.entity.Inventory;
import com.example.potato.entity.Item;
import com.example.potato.entity.User;
import com.example.potato.repository.InventoryRepository;
import com.example.potato.repository.ItemRepository;
import com.example.potato.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PurchaseConcurrencyIntegrationTest {

    private static final int CONCURRENT_REQUESTS = 100;

    @Autowired
    private PurchaseLockService purchaseLockService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RedissonClient redissonClient;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLoginId("concurrency-test-" + System.nanoTime());
        user.setPassword("test");
        user.setCurrentXp(0);
        user.setLevelStep(1);
        user.setSpoon(100);
        user = userRepository.saveAndFlush(user);

        item = new Item();
        item.setName("Concurrency Test Item");
        item.setCategory("TEST");
        item.setImageUrl("");
        item.setPrice(100);
        item.setIsLimited(false);
        item = itemRepository.saveAndFlush(item);
    }

    @AfterEach
    void tearDown() {
        if (user != null) {
            List<Inventory> inventories = inventoryRepository.findByUserId(user.getId());
            inventoryRepository.deleteAll(inventories);
            inventoryRepository.flush();
        }
        if (item != null) {
            itemRepository.deleteById(item.getId());
        }
        if (user != null) {
            userRepository.deleteById(user.getId());
        }
    }

    @Test
    void onePurchaseSucceedsAndNinetyNineAreRejectedWithoutBalanceMismatch() throws Exception {
        PurchaseRequest request = new PurchaseRequest();
        request.setUserId(user.getId());
        request.setItemId(item.getId());

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);
        CountDownLatch ready = new CountDownLatch(CONCURRENT_REQUESTS);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(CONCURRENT_REQUESTS);
        Queue<String> results = new ConcurrentLinkedQueue<>();
        Queue<Throwable> failures = new ConcurrentLinkedQueue<>();

        try {
            for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
                executor.submit(() -> {
                    ready.countDown();
                    try {
                        start.await();
                        results.add(purchaseLockService.purchaseItem(request));
                    } catch (Throwable throwable) {
                        failures.add(throwable);
                    } finally {
                        done.countDown();
                    }
                });
            }

            assertTrue(ready.await(10, TimeUnit.SECONDS), "모든 요청이 시작 준비를 마쳐야 합니다.");
            start.countDown();
            assertTrue(done.await(30, TimeUnit.SECONDS), "모든 구매 요청이 제한 시간 안에 끝나야 합니다.");
        } finally {
            executor.shutdownNow();
        }

        long successCount = results.stream().filter(result -> result.contains("구매가 완료")).count();
        long duplicateCount = results.stream().filter(result -> result.contains("이미 보유")).count();
        User savedUser = userRepository.findById(user.getId()).orElseThrow();
        List<Inventory> inventories = inventoryRepository.findByUserId(user.getId());
        RLock lock = redissonClient.getLock("lock:purchase:user:" + user.getId());

        assertTrue(failures.isEmpty(), () -> "예외가 발생했습니다: " + failures);
        assertEquals(CONCURRENT_REQUESTS, results.size());
        assertEquals(1L, successCount);
        assertEquals(CONCURRENT_REQUESTS - 1L, duplicateCount);
        assertEquals(0, savedUser.getSpoon());
        assertEquals(1, inventories.size());
        assertFalse(lock.isLocked(), "구매 완료 후 분산 락이 해제되어야 합니다.");
    }
}
