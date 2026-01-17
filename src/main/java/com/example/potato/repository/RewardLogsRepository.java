package com.example.potato.repository;

import com.example.potato.entity.RewardLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardLogsRepository extends JpaRepository<RewardLog, Long> {
}
