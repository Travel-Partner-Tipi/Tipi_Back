package com.app.sociallogin;

import com.app.entitiy.User;
import com.app.repository.UserRepository;
import com.app.sociallogin.kakao.service.KakaoService;
import com.app.sociallogin.naver.service.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final KakaoService kakaoService;
    private final NaverService naverService;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());
        model.addAttribute("naverUrl", naverService.getNaverLogin());

        return "index";
    }
    @GetMapping("/info")
    public String additionalInfoForm() {
        return "info";
    }

    @PostMapping("/info")
    @ResponseBody
    public Map<String, Object> saveAdditionalInfo(HttpSession session, @RequestParam String info1, @RequestParam String info2) {
        String email = (String) session.getAttribute("email");
        naverService.saveAdditionalInfo(email, info1, info2);
        String id = (String) session.getAttribute("id");

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);

        return response;
    }
}
