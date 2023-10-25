package com.fastcampus.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;

    // 에러 발생 시 에러코드 반환, 결과값 null
    public static Response<Void> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    //성공 시 resultCode로 SUCCESS 반환, 결과값 현재 들어온 response 타입 형태로 반환
    public static <T>Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }
}
