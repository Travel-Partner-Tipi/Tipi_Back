package com.app.crud.service;

import javax.transaction.Transactional;

import com.app.crud.dto.Postdto;
import com.app.entitiy.Post;
import com.app.repository.BlogRepository;
import com.app.sociallogin.kakao.dto.KakaoDTO;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final BlogRepository blogRepository;

    public void upload(String author, String content, String title) {
        Post post = Post.builder()
                .content(content)
                .author(author)
                .title(title)
                .created_at(LocalDateTime.now())
                .build();

        blogRepository.save(post);
    }



    public List<Post> findAll() {
        return blogRepository.findAll();
    }

    public Post findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void delete(long id) {
        Post article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

//        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

//    @Transactional
//    public Post update(long id, UpdateArticleRequest request) {
//        Post post = blogRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
//
////        authorizeArticleAuthor(post);
//        post.update(request.getTitle(), request.getContent());
//
//        return post;
//    }

    // 게시글을 작성한 유저인지 확인
//    private static void authorizeArticleAuthor(Post post) {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (!post.getAuthor().equals(userName)) {
//            throw new IllegalArgumentException("not authorized");
//        }
//    }

}
