package com.example.potato.service;

import com.example.potato.dto.PurchaseRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseLockServiceTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock lock;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private PurchaseLockService purchaseLockService;

    @Test
    void purchaseRunsInsideDistributedLockAndUnlocksAfterTransactionMethodReturns() throws Exception {
        PurchaseRequest request = request(1L, 2L);
        when(redissonClient.getLock("lock:purchase:user:1")).thenReturn(lock);
        when(lock.tryLock(10L, TimeUnit.SECONDS)).thenReturn(true);
        when(itemService.purchaseItem(request)).thenReturn("구매가 완료되었습니다!");
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        String result = purchaseLockService.purchaseItem(request);

        assertEquals("구매가 완료되었습니다!", result);
        InOrder order = inOrder(lock, itemService);
        order.verify(lock).tryLock(10L, TimeUnit.SECONDS);
        order.verify(itemService).purchaseItem(request);
        order.verify(lock).isHeldByCurrentThread();
        order.verify(lock).unlock();
    }

    @Test
    void purchaseFailsWithoutStartingTransactionWhenLockCannotBeAcquired() throws Exception {
        PurchaseRequest request = request(1L, 2L);
        when(redissonClient.getLock("lock:purchase:user:1")).thenReturn(lock);
        when(lock.tryLock(10L, TimeUnit.SECONDS)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> purchaseLockService.purchaseItem(request));

        verify(itemService, never()).purchaseItem(request);
        verify(lock, never()).unlock();
    }

    private PurchaseRequest request(Long userId, Long itemId) {
        PurchaseRequest request = new PurchaseRequest();
        request.setUserId(userId);
        request.setItemId(itemId);
        return request;
    }
}
