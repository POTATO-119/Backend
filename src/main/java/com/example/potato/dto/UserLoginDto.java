package com.example.potato.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDto {

    //로그인 정보
    private String loginId;
    private String password;
}
