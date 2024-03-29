package com.app.sociallogin.kakao.service;

import com.app.entitiy.RfToken;
import com.app.entitiy.User;
import com.app.repository.RefreshTokenRepository;
import com.app.repository.UserRepository;
import com.app.sociallogin.kakao.dto.KakaoDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
public class KakaoService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public KakaoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    public KakaoDTO getKakaoInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-type", "application/x-www-form-urlencoded");

	        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	        params.add("grant_type"   , "authorization_code");
	        params.add("client_id"    , KAKAO_CLIENT_ID);
	        params.add("client_secret", KAKAO_CLIENT_SECRET);
	        params.add("code"         , code);
	        params.add("redirect_uri" , KAKAO_REDIRECT_URL);

	        RestTemplate restTemplate = new RestTemplate();
	        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

	        ResponseEntity<String> response = restTemplate.exchange(
	        		KAKAO_AUTH_URI + "/oauth/token",
	                HttpMethod.POST,
	                httpEntity,
	                String.class
	        );

	        JSONParser jsonParser = new JSONParser();
	        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

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
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);
        params.add("refresh_token", refreshToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_AUTH_URI + "/oauth2.0/token",
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
    private KakaoDTO getUserInfoWithToken(String accessToken,String refreshtoken) throws Exception {
        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        //Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));
        saveRefreshToken(email,refreshtoken);
        return KakaoDTO.builder()
                .access(accessToken)
                .refreshtoken(refreshtoken)
                .id(id)
                .email(email)
                .nickname(nickname).build();
    }
    public boolean saveUserInfo(KakaoDTO kakaoDTO) {

        try {
            User user = userRepository.findByUserid(kakaoDTO.getEmail());

            if (user == null) {
                user = new User();
                user.setUserid(kakaoDTO.getEmail());
                user.setName(kakaoDTO.getNickname());
                user.setAccess(kakaoDTO.getAccess());

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
