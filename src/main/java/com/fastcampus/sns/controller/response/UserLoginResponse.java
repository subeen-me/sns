package com.fastcampus.sns.controller.response;

import com.fastcampus.sns.model.User;
import com.fastcampus.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 응답 전용 User dto
 */
@Getter
@AllArgsConstructor
public class UserLoginResponse {

    private String token;

}
