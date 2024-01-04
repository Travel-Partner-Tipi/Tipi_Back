package com.app.entitiy;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.awt.*;
import java.util.Map;

@Entity
public class User {

    @Id
    private String access; //엑세스 토큰
    private String userid;  // 이메일 주소
    private String userpw;  // 사용자 비밀번호
    private String name;  // 사용자 이름
    private String picture;  // 사용자 프로필 사진 URL
    private String type;  // 계정 유형(일반, 관리자 등)
    private String nickname; // 별명
    private String info1;  // 추가정보
    private String info2;  // 추가정
    private Integer follower;   // 팔로워 수 (정수 가정)
    private Integer follow;     // 팔로우 수 (정수 가정)

   /* getter와 setter 메서드들 */
    public String getNickname() { return this.nickname;}
    public String getAccess() { return this.access; }
    public String getUserid() {
       return this.userid;
   }
    public String getUserpw() {
        return this.userpw;
    }
    public String getName() {
        return this.name;
    }
    public String getPicture() {
        return this.picture;
    }
    public String getType() {
        return this.type;
    }

    public void setUserid(String userid) {
        this.userid = userid;
   }

   public void setUserpw(String userpw) {
        this.userpw = userpw;
   }

   public void setName(String name) {
        this.name = name;
   }
   public void setAccess(String access) { this.access = access; }
   public void setPicture(String picture) {
        this.picture = picture;
   }

   public void setType(String type) {
        this.type = type;
   }
   
   public void setFollower(Integer follower) {
       this.follower = follower;
   }
   public void update(String info1, String info2) {
       this.info1 = info1;
       this.info2 = info2;
    }

   public void NicknameUpdate(String nickname,String picture) {
        this.nickname=nickname;
        this.picture=picture;
   }
}
