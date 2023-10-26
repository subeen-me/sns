package com.fastcampus.sns.service;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.model.User;
import com.fastcampus.sns.model.entity.UserEntity;
import com.fastcampus.sns.repository.UserEntityRepository;
import com.fastcampus.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadByUserName(String userName) {
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", userName)));
    }


    @Transactional
    public User join(String userName, String password) {
        //회원가입하려는 UserName으로 회원가입된 user가 있는지
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        //회원가입 진행 = user를 등록
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));
       // throw new RuntimeException(); //test

        return User.fromEntity(userEntity); //userEntity를 dto 반환, 리턴
    }

    // TODO : implement
    public String login(String userName, String password) {
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(()-> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        //비밀번호 체크
        // if(!userEntity.getPassword().equals(password)) { //encoder 안썼을 때 패스워드 비교
        if(!encoder.matches(password, userEntity.getPassword())){
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성 후 반환
        return JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
    }
}
