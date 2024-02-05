package com.app.sociallogin.kakao.controller;

import com.app.entitiy.RfToken;
import com.app.repository.RefreshTokenRepository;
import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.kakao.dto.KakaoDTO;
import com.app.sociallogin.kakao.service.KakaoService;
import com.app.sociallogin.naver.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
public class KakaoController {

    @Autowired
    private final KakaoService kakaoService;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/callback")
    @ResponseBody
    public ResponseEntity<KakaoDTO> callback(HttpServletRequest request , HttpServletResponse response) {
        try {
            String code = request.getParameter("code");
            KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code);
            boolean isFirstTimeUser = kakaoService.saveUserInfo(kakaoInfo);
            HttpSession session = request.getSession();
            session.setAttribute("email", kakaoInfo.getEmail());
            session.setMaxInactiveInterval(1800); // 30분 유지
            CookieUtil.addCookie(response,"accesstoken",kakaoInfo.getAccess(),1000);
            CookieUtil.addCookie(response,"refreshtoken",kakaoInfo.getRefreshToken(),1000);
            return ResponseEntity.status(303)
                    .header("Location", "/kakao?access=" + kakaoInfo.getAccess() )
                    .build();
//            if (isFirstTimeUser) {
//                response.sendRedirect("/signup1");
//                System.out.println("처음 사용자");
//
//            } else {
//                response.sendRedirect("/signup1");
//                System.out.println("등록된 사용자");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(null); // 실패 시 에러 메시지 반환
        }
    }

}
