package com.app.sociallogin.naver.dto;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverDTO {

    private String id;
    private String email;
    private String name;
    private String access;
    public String getAccess(){ return access;}
    public String getId() {return id; }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
