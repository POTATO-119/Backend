package com.example.potato.controller;

import com.example.potato.dto.UserJoinDto;
import com.example.potato.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     * [POST] /join
     * @param dto 회원가입 요청 데이터 (아이디, 비밀번호)
     * @return 가입 성공 메시지
     */
    @PostMapping("/join")
    public String join(@RequestBody UserJoinDto dto) {

        // 서비스 계층의 회원가입 로직 호출
        userService.join(dto);

        return "회원가입 성공!";
    }
}