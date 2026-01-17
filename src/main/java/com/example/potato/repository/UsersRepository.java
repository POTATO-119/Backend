package com.example.potato.repository;

import com.example.potato.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {

    User findByLoginId(String loginId);
}
