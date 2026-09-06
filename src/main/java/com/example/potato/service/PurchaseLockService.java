package com.example.potato.service;

import com.example.potato.dto.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PurchaseLockService {

    private static final String LOCK_PREFIX = "lock:purchase:user:";
    private static final long LOCK_WAIT_SECONDS = 10L;

    private final RedissonClient redissonClient;
    private final ItemService itemService;

    public String purchaseItem(PurchaseRequest request) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + request.getUserId());
        boolean acquired = false;

        try {
            acquired = lock.tryLock(LOCK_WAIT_SECONDS, TimeUnit.SECONDS);
            if (!acquired) {
                throw new IllegalStateException("구매 요청이 많아 락 획득에 실패했습니다.");
            }

            return itemService.purchaseItem(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("구매 처리 중 인터럽트가 발생했습니다.", e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
