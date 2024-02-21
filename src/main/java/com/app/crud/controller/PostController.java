package com.app.crud.controller;

import com.app.crud.dto.Postdto;
import com.app.crud.service.PostService;
import com.app.entitiy.Post;
import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.kakao.dto.KakaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
@Controller
@RequiredArgsConstructor
public class PostController {
    @Autowired
    private final PostService postService;

    @PostMapping("/post/upload")
    public ResponseEntity<KakaoDTO> uploadPost(String author, String content, String title){
        postService.upload(author,content,title);
        return ResponseEntity.ok()
            .header("Access", "Bearer " + author)
            .build();
    }

    @GetMapping("/main")
    public String main(HttpSession session) {
    try{
        return "main";

    } catch (Exception e) {
        e.printStackTrace();
        return "index";
    }



}}
