package com.app.sociallogin;

import com.app.repository.UserRepository;
import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.kakao.service.KakaoService;
import com.app.sociallogin.naver.dto.NaverDTO;
import com.app.sociallogin.naver.service.NaverService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private final NaverService naverService;

    @Autowired
    private final KakaoService kakaoService;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("naverUrl", naverService.getNaverLogin());
        model.addAttribute("kakaoUrl" , kakaoService.getKakaoLogin());
        return "index";
    }
    @RequestMapping(value = "/signup1" , method = RequestMethod.GET)
    public String additionalInfoForm() {
        try{

            return "signup1";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "/signup1" , method = RequestMethod.POST)
    @ResponseBody
    public void saveAdditionalInfo(HttpSession session, @RequestParam String picture,
                                                        @RequestParam String nickname,HttpServletResponse response) throws IOException {
        String email = (String) session.getAttribute("email");
        try {
            naverService.saveAdditionalInfo(email, picture, nickname);
            kakaoService.saveAdditionalInfo(email, picture, nickname);


            response.sendRedirect("/main");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/");

        }
    }
    @GetMapping("/signup2")
    public ResponseEntity<MsgEntity> signup2(HttpSession session) {
        try{
            
            return ResponseEntity.ok()
                    .body(new MsgEntity("Success", session.getAttribute("email")));

        } catch (Exception e) {
            e   .printStackTrace();
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

    @RequestMapping(value = "/token/expired" , method = RequestMethod.GET)
    public ResponseEntity<MsgEntity> renewtoken(HttpServletRequest request,String refreshtoken) throws Exception {
        try {
            String newaccesstoken = naverService.renewAccessToken(request,refreshtoken);
            return ResponseEntity.ok()
                    .body(new MsgEntity("토큰 재생성 성공", newaccesstoken));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MsgEntity("토큰 재생성 오류" , null));
        }
    }
    @RequestMapping(value = "/AuthorizateId", method = RequestMethod.GET)
    public ResponseEntity<MsgEntity> AuthorizationId(String accesstoken,HttpServletResponse response) throws Exception {
        boolean isExistUser = naverService.AuthorizationId(accesstoken);
        if(isExistUser){
            return ResponseEntity.ok()
                    .body(new MsgEntity("본인 인증 성공",accesstoken));

        }
        else {
            response.sendRedirect("/token/expired");
            return ResponseEntity.status(500)
                    .body(new MsgEntity("토큰 만료 & 재 생성","accesstoken expired"));
        }
    }

}
