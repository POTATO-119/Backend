package com.example.potato.service;

import com.example.potato.dto.UserJoinDto;
import com.example.potato.dto.UserLoginDto;
import com.example.potato.entity.User;
import com.example.potato.repository.UserRepository;
import jakarta.transaction.Transactional;
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
     * 아이디 중복 체크 API용
     * 존재하면 true, 없으면 false 반환
     */
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }


    /**
     * 로그인 로직
     * 1. 아이디로 유저 찾기
     * 2. 비밀번호 일치 여부 확인
     * 3. 아이디 검색 → 비밀번호 필터링 → 성공 시 User 반환 / 실패 시 null 반환
     *
     */
    public User login(UserLoginDto dto) {
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

    /**
     * 회원 정보 수정 (비밀번호 변경)
     */
    @Transactional
    public void updateUserInfo(String loginId, String newPassword) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setPassword(newPassword); // 감자 유저의 비밀번호 변경!
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user); // DB에서 유저 삭제 🥔👋
    }

    /**
     * 요리 인증 보상 및 레벨업 로직
     * 1. 사진/레시피 여부에 따른 경험치(XP) 및 재화(스푼) 차등 지급
     * 2. 누적 경험치에 따른 새로운 레벨 판별
     * 3. 레벨업 시 보너스 재화 지급 및 업데이트
     */
    @Transactional
    public void addExperienceAndReward(String loginId, boolean hasRecipe) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 1. 현재 상태 확인 (null 방지)
        int currentXp = user.getCurrentXp() != null ? user.getCurrentXp() : 0;
        int currentLevel = user.getLevelStep() != null ? user.getLevelStep() : 1;
        int currentSpoon = user.getSpoon();

        // 2. 인증 유형에 따른 보상 세팅 (레시피 유무)
        int addXp = hasRecipe ? 150 : 50;
        int addSpoon = hasRecipe ? 15 : 5;

        int newXp = currentXp + addXp;
        currentSpoon += addSpoon;

        // 3. 새로운 레벨 계산
        int newLevel = calculateLevel(newXp);

        // 4. 레벨업 시 보너스 스푼 지급
        if (newLevel > currentLevel) {
            currentSpoon += 50;
        }

        // 5. 업데이트된 정보 반영
        user.setCurrentXp(newXp);
        user.setLevelStep(newLevel);
        user.setSpoon(currentSpoon);
    }

    /**
     * [보조] 누적 경험치에 따른 레벨 계산
     * 10000 이상: Lv.5 (자취방 요리사)
     * 5000 이상: Lv.4 (레시피 연구원)
     * 2500 이상: Lv.3 (냉장고 탐험가)
     * 1000 이상: Lv.2 (라면 마스터)
     * 그 외: Lv.1 (자취인)
     */
    private int calculateLevel(int totalXp) {
        if (totalXp >= 10000) return 5;
        if (totalXp >= 5000) return 4;
        if (totalXp >= 2500) return 3;
        if (totalXp >= 1000) return 2;
        return 1;
    }

}