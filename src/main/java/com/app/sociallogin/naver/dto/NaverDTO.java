package com.app.sociallogin.naver.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverDTO {

    private String id;
    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
