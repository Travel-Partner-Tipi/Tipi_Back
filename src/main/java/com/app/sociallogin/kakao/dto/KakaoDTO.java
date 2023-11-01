package com.app.sociallogin.kakao.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoDTO {

    private long id;
    private String email;
    private String nickname;

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
