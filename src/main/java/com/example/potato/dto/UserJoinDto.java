package com.example.potato.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserJoinDto {

    //가입정보
    private String loginId;
    private String password;

}