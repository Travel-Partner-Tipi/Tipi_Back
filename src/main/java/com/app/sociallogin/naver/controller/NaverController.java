package com.app.sociallogin.naver.controller;


import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.kakao.dto.KakaoDTO;
import com.app.sociallogin.naver.dto.NaverDTO;
import com.app.sociallogin.naver.service.NaverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
            if (isFirstTimeUser) {
                response.sendRedirect("/info");
                System.out.println("처음 사용자");
            } else {
                response.sendRedirect("/info");
                System.out.println("등록된 사용자");
            }
            return ResponseEntity.ok()
                    .body(new MsgEntity("Success", naverInfo));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MsgEntity("Error", null)); // 실패 시 에러 메시지 반환
        }
    }

}
