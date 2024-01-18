package com.app.crud.service;

import javax.transaction.Transactional;

import com.app.crud.domain.Post;
import com.app.crud.postdto.AddPostRequest;
import com.app.crud.postdto.UpdateArticleRequest;
import com.app.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final BlogRepository blogRepository;

    public Post save(AddPostRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
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

    @Transactional
    public Post update(long id, UpdateArticleRequest request) {
        Post post = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

//        authorizeArticleAuthor(post);
        post.update(request.getTitle(), request.getContent());

        return post;
    }

    // 게시글을 작성한 유저인지 확인
//    private static void authorizeArticleAuthor(Post post) {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (!post.getAuthor().equals(userName)) {
//            throw new IllegalArgumentException("not authorized");
//        }
//    }

}
