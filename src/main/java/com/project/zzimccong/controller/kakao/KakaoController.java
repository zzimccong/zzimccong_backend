package com.project.zzimccong.controller.kakao;

import com.project.zzimccong.config.KakaoConfig;
import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/oauth")
public class KakaoController {

    private final KakaoConfig kakaoConfig;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public KakaoController(KakaoConfig kakaoConfig, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.kakaoConfig = kakaoConfig;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getUserInfo(@RequestBody Map<String, String> tokenData) {
        String accessToken = tokenData.get("access_token");
        Map<String, Object> kakaoUserInfo = kakaoConfig.fetchUserInfo(accessToken);

        String nickname = (String) kakaoUserInfo.get("nickname");
        String email = (String) kakaoUserInfo.get("email");

        log.debug("받은 카카오 사용자 정보: 닉네임={}, 이메일={}", nickname, email);

        // 이메일을 기준으로 사용자 확인
        if (userService.isEmailExists(email)) {
            log.debug("이메일 {}을(를) 가진 사용자가 존재합니다. 인증을 진행합니다...", email);
            // 기존 사용자인 경우 로그인 처리
            User existingUser = userService.getUserByNameAndEmail(nickname, email);
            String token = jwtTokenUtil.generateToken(existingUser.getLoginId(), existingUser.getRole());

            log.debug("사용자가 성공적으로 인증되었습니다. 생성된 토큰: {}", token);
            return ResponseEntity.ok(createUserResponse(token, existingUser));
        } else {
            log.debug("이메일 {}을(를) 가진 사용자가 존재하지 않습니다. 회원가입을 위해 401 상태를 반환합니다.", email);
            // 신규 사용자일 경우 추가 정보를 받아 회원가입 유도
            UserDTO userDTO = buildUserDTO(null, email, nickname, null, email, null, "USER");
            log.debug("Returning UserDTO with loginId={}, name={}, email={}", email, nickname, email);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userDTO);
        }
    }

    @PostMapping("/kakao-register")
    public ResponseEntity<?> registerKakaoUser(@RequestBody UserDTO userDTO) {
        try {
            // 회원가입 처리
            User newUser = userService.registerUserWithoutPassword(userDTO);

            // JWT 토큰 발급
            String token = jwtTokenUtil.generateToken(newUser.getLoginId(), newUser.getRole());

            log.debug("사용자가 성공적으로 등록되었습니다. 생성된 토큰: {}", token);
            return ResponseEntity.ok(createUserResponse(token, newUser));
        } catch (Exception e) {
            log.error("카카오톡 회원가입 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카카오톡 회원가입 실패: " + e.getMessage());
        }
    }

    // 중복된 코드 제거: UserDTO를 생성하는 메서드
    private UserDTO buildUserDTO(Integer id, String loginId, String name, String birth, String email, String phone, String role) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setLoginId(loginId);
        userDTO.setName(name);
        userDTO.setBirth(birth);
        userDTO.setEmail(email);
        userDTO.setPhone(phone);
        userDTO.setRole(role);
        return userDTO;
    }

    // 응답을 생성하는 메서드
    private Map<String, Object> createUserResponse(String token, User user) {
        UserDTO responseUserDTO = buildUserDTO(
                user.getId(),
                user.getLoginId(),
                user.getName(),
                user.getBirth() != null ? user.getBirth().toString() : null,
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", responseUserDTO);

        return response;
    }
}
