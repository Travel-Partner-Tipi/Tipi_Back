package com.app.sociallogin.naver.service;

import com.app.entitiy.RfToken;
import com.app.entitiy.User;
import com.app.repository.RefreshTokenRepository;
import com.app.repository.UserRepository;
//import com.app.sociallogin.naver.config.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.app.sociallogin.naver.dto.NaverDTO;
import com.app.sociallogin.naver.util.CookieUtil;
import groovy.util.logging.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.app.repository.UserRepository;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.awt.*;
import java.time.Duration;
import java.util.Map;


@Service
public class NaverService {
    @Autowired
    private final UserRepository userRepository;
    private static Logger logger = LoggerFactory.getLogger(NaverService.class);
    public NaverService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${naver.redirect.url}")
    private String NAVER_REDIRECT_URL;

    public static final String REFRESH_TOKEN_COOKIE_NAME= "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    private final static String NAVER_AUTH_URI = "https://nid.naver.com";
    private final static String NAVER_API_URI = "https://openapi.naver.com";

//    private OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    //postmapping
    public String getNaverLogin() {
        return NAVER_AUTH_URI + "/oauth2.0/authorize"
                + "?client_id=" + NAVER_CLIENT_ID
                + "&redirect_uri=" + NAVER_REDIRECT_URL
                + "&response_type=code";
    }

    public NaverDTO getNaverInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-type", "application/x-www-form-urlencoded");

	        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	        params.add("grant_type"   , "authorization_code");
	        params.add("client_id"    , NAVER_CLIENT_ID);
	        params.add("client_secret", NAVER_CLIENT_SECRET);
	        params.add("code"         , code);
	        params.add("redirect_uri" , NAVER_REDIRECT_URL);

	        RestTemplate restTemplate = new RestTemplate();
	        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

	        ResponseEntity<String> response1 = restTemplate.exchange(
	        		NAVER_AUTH_URI + "/oauth2.0/token",
	                HttpMethod.POST,
	                httpEntity,
	                String.class
	        );

	        JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObj = (JSONObject) jsonParser.parse(response1.getBody());

            accessToken  = (String) jsonObj.get("access_token");
            refreshToken = (String) jsonObj.get("refresh_token");

        } catch (Exception e) {
            throw new Exception("API call failed");
        }

        return getUserInfoWithToken(accessToken,refreshToken);
    }
    private void saveRefreshToken(String userId, String newRefreshToken){
        RfToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RfToken(userId, newRefreshToken));
//        RefreshToken refreshToken = new RefreshToken(userId,newRefreshToken);
        refreshTokenRepository.save(refreshToken);

    }
    public boolean AuthorizationId(String accesstoken) throws Exception {
        User user = userRepository.findByAccess(accesstoken);
        if(user != null){
            return true;
        }
        else{
            return false;
        }
    }
    public String renewAccessToken(HttpServletRequest request , String refreshToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("refresh_token", refreshToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_AUTH_URI + "/oauth2.0/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        String email = "ppdoli123@naver.com";
        String accesstoken = (String) jsonObj.get("access_token");
        System.out.println(email);
        User user = userRepository.findByUserid(email);
        user.reNewAccessToken(accesstoken);
        return accesstoken;
    }


    public void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response,
    String refreshToken){
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response,REFRESH_TOKEN_COOKIE_NAME,refreshToken,cookieMaxAge);
    }

    private NaverDTO getUserInfoWithToken(String accessToken,String refreshToken) throws Exception {
        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                NAVER_API_URI + "/v1/nid/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );


        //Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("response");

        String id = String.valueOf(account.get("id"));
        String email = String.valueOf(account.get("email"));
        String name = String.valueOf(account.get("name"));
        saveRefreshToken(email,refreshToken);
        return NaverDTO.builder()
                .access(accessToken)
                .id(id)
                .email(email)
                .name(name).build();
    }



    public boolean saveUserInfo(NaverDTO naverDTO) {
        try {
            User user = userRepository.findByUserid(naverDTO.getEmail());

            if (user == null) {
                user = new User();
                user.setUserid(naverDTO.getEmail());
                user.setName(naverDTO.getName());
                user.setAccess(naverDTO.getAccess());

                userRepository.save(user);

                System.out.println("User saved successfully: " + user);
                return true; // 최초 로그인인 경우 true를 반환
            }
            System.out.println("이미 가입된 사용자");
            return false; // 이미 가입된 사용자인 경우 false를 반환
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 예외가 발생한 경우 false를 반환
        }
    }
    public void saveAdditionalInfo(String email, String picture, String nickname) {

        try {
            User user = userRepository.findByUserid(email);
            user.update(picture, nickname);
            userRepository.save(user);
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }
    public void saveNicknameInfo(String email,String nickname,String picture) {

        User user = userRepository.findByUserid(email);
        user.NicknameUpdate(nickname,picture);
        userRepository.save(user);
    }


}
