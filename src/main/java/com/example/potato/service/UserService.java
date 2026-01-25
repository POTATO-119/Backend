package com.example.potato.service;

import com.example.potato.dto.UserJoinDto;
import com.example.potato.entity.User;
import com.example.potato.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입 로직
     * 1. 아이디 중복 검사
     * 2. DTO -> Entity 변환 및 초기값 설정
     * 3. DB 저장
     */
    public void join(UserJoinDto dto) {

        // 1. 아이디 중복 검증
        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        // 2. User 엔티티 생성 및 데이터 매핑
        User user = new User();
        user.setLoginId(dto.getLoginId());
        user.setPassword(dto.getPassword());

        // 초기 계정 설정 (레벨 1, 재화 0, 경험치 0)
        user.setLevelStep(1);
        user.setSpoonAmount(0);
        user.setCurrentXp(0);

        // 3. DB 저장
        userRepository.save(user);
    }
}