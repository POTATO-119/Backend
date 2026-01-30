package com.example.potato.controller;

import com.example.potato.dto.UserJoinDto;
import com.example.potato.dto.UserLoginDto;
import com.example.potato.entity.User;
import com.example.potato.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User (회원)", description = "회원가입 및 로그인 관리 API")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     * [POST] /join
     * 
     * @param dto 회원가입 요청 데이터 (아이디, 비밀번호)
     * @return 가입 성공 메시지
     */
    @Operation(summary = "회원가입", description = "계정을 생성합니다.")
    @PostMapping("/join")
    public String join(@RequestBody UserJoinDto dto) {

        // 서비스 계층의 회원가입 로직 호출
        userService.join(dto);

        return "회원가입 성공!";
    }


    /**
     * 로그인 API
     * [POST] /login
     */
    @Operation(summary = "로그인", description = "아이디와 비밀번호를 입력받아 로그인을 진행합니다.")
    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto loginDto) {
        // 1. 서비스의 로그인 로직을 호출해
        User loginUser = userService.login(loginDto);

        // 2. 결과가 null이면 로그인 실패!
        if (loginUser == null) {
            return "로그인 실패: 아이디 또는 비밀번호를 확인해주세요.";
        }

        // 3. 결과가 있으면 로그인 성공!
        return loginUser.getLoginId() + "님, 환영합니다! (로그인 성공)";
    }

    /**
     * 회원정보 조회
     * [GET] /info/{loginId}
     */
    @Operation(summary = "내 정보 조회", description = "마이페이지 등에 필요한 유저 정보를 가져옵니다.")
    @GetMapping("/info/{loginId}")
    public User getUserInfo(@PathVariable String loginId) {
        return userService.getUserInfo(loginId);
    }

}