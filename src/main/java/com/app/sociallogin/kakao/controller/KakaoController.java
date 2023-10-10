package com.app.sociallogin.kakao.controller;

import com.app.sociallogin.common.MsgEntity;
import com.app.sociallogin.kakao.dto.KakaoDTO;
import com.app.sociallogin.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) {
        try {
            String code = request.getParameter("code");
            KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code);
            
            // 여기서 DB에 저장
            kakaoService.saveUserInfo(kakaoInfo);
    
            return ResponseEntity.ok()
                    .body(new MsgEntity("Success", kakaoInfo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new MsgEntity("Error", null)); // 실패 시 에러 메시지 반환
        }
    }
    
}
