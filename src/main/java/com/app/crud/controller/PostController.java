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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    private final PostService postService;
    @GetMapping("/main")
    public String main(Model model) {
        List<Post> postList = postService.findAll();
        model.addAttribute("postList", postList);
        return "main";
    }

    @GetMapping("detail")
    public String detail(Model model, @RequestParam Long id,HttpSession session) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        String email = (String) session.getAttribute("email");
        boolean checkid = postService.updatecheck(id,email);
        model.addAttribute("checkid",checkid);

        return "postdetail";
    }
    @RequestMapping(value = "update" , method = RequestMethod.GET)
    public String update(Model model, HttpSession session,@RequestParam long id){
        session.setAttribute("postId", id);
        try{
            model.addAttribute("postid",id);
            return "postupdate";
        }
        catch(Exception e){
            e.printStackTrace();
            return "main";
        }
    }
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void updatepost(HttpSession session,String content, String title,HttpServletResponse response) throws IOException{
        Long id = (Long) session.getAttribute("postId");
        postService.update(title, content, id);
        session.removeAttribute("postId");
        try {
            response.sendRedirect("detail?id=" + id);

        }
        catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("/");
        }
    }
    @RequestMapping(value =  "/upload" , method = RequestMethod.GET)
    public String upload(HttpSession session) {
        try{
            return "postupload";
        }
        catch (Exception e){
            e.printStackTrace();
            return "main";
        }
    }
    @PostMapping("upload")
    public void uploadPost(HttpServletResponse response, HttpSession session, String content, String title)throws IOException {
        String email = (String) session.getAttribute("email");
        postService.upload(email,content,title);
        try {
            response.sendRedirect("/post/main");

        }
        catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("/");
        }
    }
    @RequestMapping(value = "delete",method = RequestMethod.GET)
    public void delete(HttpServletResponse response,@RequestParam long id) throws IOException{
        postService.delete(id);
        response.sendRedirect("/post/main");
    }




}
