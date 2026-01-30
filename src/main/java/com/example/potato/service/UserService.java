package com.example.potato.service;

import com.example.potato.dto.UserJoinDto;
import com.example.potato.dto.UserLoginDto;
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


    /**
     * 로그인 로직
     * 1. 아이디로 유저 찾기
     * 2. 비밀번호 일치 여부 확인
     * 3. 아이디 검색 → 비밀번호 필터링 → 성공 시 User 반환 / 실패 시 null 반환
     * */
    public User login(UserLoginDto dto){
        return userRepository.findByLoginId(dto.getLoginId())
                .filter(user -> user.getPassword().equals(dto.getPassword()))
                .orElse(null);
    }


    /**
     * 내 정보 조회
     * loginId로 유저 엔티티 전체를 가져옴
     */
    public User getUserInfo(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

}