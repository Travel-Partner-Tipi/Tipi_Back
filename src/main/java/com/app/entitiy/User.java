package com.app.entitiy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;
import java.util.Map;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "access", updatable = false)
    private String access; //엑세스 토큰
    @Column(name = "userid", updatable = false)
    private String userid;  // 이메일 주소
    private String userpw;  // 사용자 비// 밀번호
    @Column(name = "name", updatable = false)
    private String name;  // 사용자 이름
    private String picture;  // 사용자 프로필 사진 URL
    private String type;  // 계정 유형(일반, 관리자 등)
    @Column(name = "nickname", updatable = false)
    private String nickname; // 별명
    private Integer follower;   // 팔로워 수 (정수 가정)
    private Integer follow;     // 팔로우 수 (정수 가정)

   /* getter와 setter 메서드들 */
   public User update(String access){
       this.access = access;

       return this;
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
   public void setAccess(String access) {
        this.access = access;
    }
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
       this.nickname = info1;
       this.picture = info2;
    }

   public void NicknameUpdate(String nickname,String picture) {
        this.nickname=nickname;
        this.picture=picture;
   }

}
