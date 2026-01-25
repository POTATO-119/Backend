package com.example.potato.repository;

import com.example.potato.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //1.로그인용 (유저 정보 가져오기)
    User findByLoginId(String loginId);

    //2.회원가입용 (아이디 중복 검사하기)
    boolean existsByLoginId(String loginId);
}
