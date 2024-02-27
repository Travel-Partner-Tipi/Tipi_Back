package com.app.crud.dto;

import com.app.entitiy.Post;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class Postdto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    private String author;


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public String getAuthor() {
        return author;
    }

    public Postdto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.created_at = post.getCreated_at();
        this.updated_at = post.getUpdated_at();
        this.author = post.getAuthor();
    }
}