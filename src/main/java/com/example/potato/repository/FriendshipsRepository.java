package com.example.potato.repository;

import com.example.potato.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipsRepository extends JpaRepository<Friendship, Long> {
}
