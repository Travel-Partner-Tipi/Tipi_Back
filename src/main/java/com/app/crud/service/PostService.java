package com.app.crud.service;

import javax.transaction.Transactional;

import com.app.crud.dto.Postdto;
import com.app.entitiy.Post;
import com.app.entitiy.RfToken;
import com.app.repository.BlogRepository;
import com.app.sociallogin.kakao.dto.KakaoDTO;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostService {

    @Autowired
    private final BlogRepository blogRepository;

    public void upload(String id, String content, String title) {
//        try {
        Post post = Post.builder()
                .content(content)
                .author(id)
                .title(title)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();


        blogRepository.save(post);
//            return ResponseEntity.ok().body("success");
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("쿼리문 저장 실패");
//
//        }
    }

    public void update(String title, String content, long id) {
        Post post = blogRepository.findById(id)
                .map(entity -> entity.update(title, content,LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        blogRepository.save(post);
////        RefreshToken refreshToken = new RefreshToken(userId,newRefreshToken);
//        blogRepository.save(content);
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

    public boolean updatecheck(long id, String email) {
        Post post = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        if (Objects.equals(post.getAuthor(), email)) {
            return true;
        } else {
            return false;
        }
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
