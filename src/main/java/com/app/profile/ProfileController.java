package com.app.profile;

import com.app.entitiy.User;
import com.app.repository.UserRepository;
import com.app.sociallogin.naver.service.NaverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        String email = (String)session.getAttribute("email");
        User user = userRepository.findByUserid(email);
        model.addAttribute("user", user);
        return "profile";
    }
}
