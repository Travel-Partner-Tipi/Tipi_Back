package com.app.entitiy;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private String userid;  // 이메일 주소
    private String userpw;  // 사용자 비밀번호
    private String name;  // 사용자 이름
    private String picture;  // 사용자 프로필 사진 URL
    private String type;  // 계정 유형(일반, 관리자 등)
    private Integer follower;   // 팔로워 수 (정수 가정)
    private Integer follow;     // 팔로우 수 (정수 가정)

   /* getter와 setter 메서드들 */
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

   public void setPicture(String picture) {
        this.picture = picture;
   }

   public void setType(String type) {
        this.type = type;
   }
   
   public void setFollower(Integer follower) {
       this.follower = follower;
   }
   public void update(String userpw, String picture) {
       this.userpw = userpw;
       this.picture = picture;
    }
   
}
