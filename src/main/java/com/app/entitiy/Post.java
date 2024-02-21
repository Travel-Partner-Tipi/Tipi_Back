package com.app.entitiy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "author" , updatable = false)
    private String author;

    @Column(name = "title" , updatable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Post(String author, String title, String content, LocalDateTime created_at) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
//    public Post update(TimeStamp created_at){
//        this.created_at = created_at;
//    }
}
