package com.app.sociallogin.naver.controller;


import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.naver.dto.NaverDTO;
import com.app.sociallogin.naver.service.NaverService;
import com.app.sociallogin.naver.util.CookieUtil;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("naver")
public class NaverController {

    private final NaverService naverService;



    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            NaverDTO naverInfo = naverService.getNaverInfo(request.getParameter("code"));
            // 여기서 DB에 저장
            boolean isFirstTimeUser = naverService.saveUserInfo(naverInfo);
            session.setAttribute("email", naverInfo.getEmail());
            CookieUtil.addCookie(response,"accesstoken",naverInfo.getAccess(),10);
            CookieUtil.addCookie(response,"refreshtoken",naverInfo.getAccess(),30);
            return ResponseEntity.ok()
                    .body(new MsgEntity("Success", naverInfo));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MsgEntity("Error", null)); // 실패 시 에러 메시지 반환
        }
    }

}
