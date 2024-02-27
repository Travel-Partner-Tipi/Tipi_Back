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

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Builder
    public Post(String author, String title, String content, LocalDateTime created_at, LocalDateTime updated_at) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Post update(String title, String content, LocalDateTime updated_at) {
        this.title = title;
        this.content = content;
        this.updated_at = updated_at;
        return this;
    }
//    public Post update(TimeStamp created_at){
//        this.created_at = created_at;
//    }
}
