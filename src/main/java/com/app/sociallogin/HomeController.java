package com.app.sociallogin;

import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.naver.service.NaverService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Controller
//@Slf4j
public class HomeController {
//    private static Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final NaverService naverService;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("naverUrl", naverService.getNaverLogin());

        return "index";
    }
    @GetMapping("/signup1")
    public String additionalInfoForm() {
        return "/signup1";
    }

    @PostMapping("/signup1")
    @ResponseBody
    public String saveAdditionalInfo(HttpSession session, @RequestParam String info1, @RequestParam String info2, HttpServletResponse response) throws IOException {
        String email = (String) session.getAttribute("email");

        naverService.saveAdditionalInfo(email, info1 , info2);

        response.sendRedirect("/signup2");
        return "/signup2";
    }
    @GetMapping("/signup2")
    public ResponseEntity<MsgEntity> signup2(HttpSession session) {
        try{
            
            return ResponseEntity.ok()
                    .body(new MsgEntity("Success", session.getAttribute("email")));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MsgEntity("Error", null)); // 실패 시 에러 메시지 반환
        }
    }
    @PostMapping("/signup2")
    public String image_nickname(HttpSession session, String nickname, String picture){
        String email = (String) session.getAttribute("email");

        naverService.saveNicknameInfo(email,nickname,picture);
        return "/profile";

    }

}
