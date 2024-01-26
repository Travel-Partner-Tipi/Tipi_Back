package com.app.entitiy;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RfToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "client_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public RfToken(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }


    public RfToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;

        return this;
    }
}
