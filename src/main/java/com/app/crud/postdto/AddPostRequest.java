package com.app.crud.postdto;
import com.app.crud.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddPostRequest {
    private String title;

    private String content;

    public Post toEntity(String author) {
        return Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}