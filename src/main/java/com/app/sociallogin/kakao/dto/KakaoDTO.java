package com.app.sociallogin.kakao.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoDTO {

    private Long id;
    private String email;
    private String nickname;
    private String access;
    private String refreshtoken;
    public String getAccess(){ return access;}
    public Long getId() {return id; }

    public String getRefreshToken() { return refreshtoken;}

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}