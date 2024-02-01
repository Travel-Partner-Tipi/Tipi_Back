package com.app.sociallogin.naver.controller;


import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.naver.dto.NaverDTO;
import com.app.sociallogin.naver.service.NaverService;
import com.app.sociallogin.naver.util.CookieUtil;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("naver")
public class NaverController {

    @Autowired
    private final NaverService naverService;



    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request, HttpServletResponse response) {
        try {
            NaverDTO naverInfo = naverService.getNaverInfo(request.getParameter("code"));
            // 여기서 DB에 저장
            boolean isFirstTimeUser = naverService.saveUserInfo(naverInfo);
            HttpSession session = request.getSession();
            session.setAttribute("email", naverInfo.getEmail());
            session.setMaxInactiveInterval(1800); // 30분 유지
            CookieUtil.addCookie(response,"accesstoken",naverInfo.getAccess(),1000);
            CookieUtil.addCookie(response,"refreshtoken",naverInfo.getRefreshToken(),1000);

            if (isFirstTimeUser) {
                response.sendRedirect("/signup1");
                System.out.println("처음 사용자");
            } else {
                response.sendRedirect("/signup1");
                System.out.println("등록된 사용자");
            }
            return ResponseEntity.ok()
                    .body(new MsgEntity("회원가입 성공", naverInfo));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MsgEntity("회원가입 오류", null)); // 실패 시 에러 메시지 반환
        }
    }



}
