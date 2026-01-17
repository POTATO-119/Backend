package com.example.potato.repository;

import com.example.potato.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
}
