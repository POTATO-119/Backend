package com.example.potato.repository;

import com.example.potato.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :userId")
    Optional<User> findByIdForUpdate(@Param("userId") Long userId);

    //1.로그인용 (유저 정보 가져오기)
    Optional<User> findByLoginId(String loginId);

    //2.회원가입용 (아이디 중복 검사하기)
    boolean existsByLoginId(String loginId);
}
