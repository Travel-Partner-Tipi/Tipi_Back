//package com.app.profile;
//
//import com.app.repository.UserRepository;
//import com.app.sociallogin.common.MsgEntity;
//import com.app.sociallogin.naver.service.NaverService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import javax.servlet.http.HttpSession;
//
//@Controller
//public class ProfileController {
//    private final NaverService naverservice;
//    private final UserRepository userRepository;
//    public ProfileController(NaverService naverservice, UserRepository userRepository) {
//        this.naverservice = naverservice;
//        this.userRepository = userRepository;
//    }
//    @GetMapping("/profile")
//    public ResponseEntity<MsgEntity> profile(HttpSession session) {
//    try{
//        return ResponseEntity.ok()
//                .body(new MsgEntity("Success", session.getAttribute("email")));
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(500)
//                .body(new MsgEntity("Error", null)); // 실패 시 에러 메시지 반환
//    }
//    }
//}
