package com.project.zzimccong.controller.user;

import com.project.zzimccong.model.dto.sms.SmsVerificationDTO;
import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.notification.NotificationService;
import com.project.zzimccong.service.redis.RefreshTokenService;
import com.project.zzimccong.service.redis.TemporaryStorageService;
import com.project.zzimccong.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final TemporaryStorageService temporaryStorageService;

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    private final NotificationService notificationService;

    public UserController(TemporaryStorageService temporaryStorageService, RefreshTokenService refreshTokenService, UserService userService, JwtTokenUtil jwtTokenUtil, NotificationService notificationService) {
        this.temporaryStorageService = temporaryStorageService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.notificationService = notificationService;
    }

    @PostMapping("/user-register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("사용자 등록에 성공했습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 등록에 실패했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/check-id")
    public ResponseEntity<?> checkLoginId(@RequestParam String loginId) {
        try {
            boolean exists = userService.isLoginIdExists(loginId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("아이디 확인 실패: " + e.getMessage());
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userService.isEmailExists(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 확인 실패: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.authenticate(userDTO.getLoginId(), userDTO.getPassword());
            if (user != null) {
                String token = jwtTokenUtil.generateToken(user.getLoginId(), "user");
                String refreshToken = jwtTokenUtil.generateRefreshToken(user.getLoginId());
                refreshTokenService.saveRefreshToken(user.getLoginId(), refreshToken); // 리프레시 토큰 저장

                Map<String, Object> response = createLoginResponse(token, refreshToken, user);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        if (jwtTokenUtil.validateToken(refreshToken)) {
            String userId = jwtTokenUtil.getUserIdFromToken(refreshToken);
            String storedToken = refreshTokenService.getRefreshToken(userId);
            if (refreshToken.equals(storedToken)) {
                String newAccessToken = jwtTokenUtil.generateToken(userId, "user");
                Map<String, Object> response = new HashMap<>();
                response.put("token", newAccessToken);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    private Map<String, Object> createLoginResponse(String token, String refreshToken, User user) {
        UserDTO responseUserDTO = new UserDTO(
                user.getId(),
                user.getLoginId(),
                null,
                user.getName(),
                user.getBirth().toString(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("refreshToken", refreshToken);
        response.put("user", responseUserDTO);
        return response;
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<User> getUserById(@PathVariable String loginId) {
        try {
            User user = userService.getUserById(loginId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{loginId}")
    public ResponseEntity<String> updateUser(@PathVariable String loginId, @RequestBody UserDTO userDTO) {
        try {
            userDTO.setLoginId(loginId);
            userService.updateUser(userDTO);
            return ResponseEntity.ok("업데이트 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트 실패: " + e.getMessage());
        }
    }

    @PutMapping("/{loginId}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable String loginId, @RequestBody Map<String, String> passwordMap) {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");

        try {
            userService.changePassword(loginId, oldPassword, newPassword);
            return ResponseEntity.ok("비밀번호 변경 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 실패: " + e.getMessage());
        }
    }

    @PostMapping("/{loginId}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable String loginId, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        try {
            boolean isDeleted = userService.deleteUser(loginId, password);
            if (isDeleted) {
                return ResponseEntity.ok("계정 삭제 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 비밀번호");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("계정 삭제 실패: " + e.getMessage());
        }
    }

    @PostMapping("/send-sms")
    public ResponseEntity<?> sendSmsVerification(@RequestBody SmsVerificationDTO smsVerificationDTO) {
        try {
            userService.sendSmsVerification(smsVerificationDTO.getPhone());
            return ResponseEntity.ok("SMS 전송 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SMS 전송 실패: " + e.getMessage());
        }
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<?> verifySmsCode(@RequestBody SmsVerificationDTO smsVerificationDTO) {
        try {
            boolean isValid = userService.verifySmsCode(smsVerificationDTO.getPhone(), smsVerificationDTO.getVerificationCode());
            if (isValid) {
                return ResponseEntity.ok("인증 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 인증 코드");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 실패: " + e.getMessage());
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.getUserByNameAndEmail(userDTO.getName(), userDTO.getEmail());
            if (user != null) {
                return ResponseEntity.ok(user.getLoginId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("아이디 찾기 실패: " + e.getMessage());
        }
    }

    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.sendTemporaryPassword(userDTO.getLoginId(), userDTO.getEmail());
            return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("임시 비밀번호 전송 실패: " + e.getMessage());
        }
    }

    @PostMapping("/delete-sms")
    public ResponseEntity<Void> deleteSmsVerificationCode(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        temporaryStorageService.deleteSMSVerificationCode(phone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-email")
    public ResponseEntity<Void> deleteEmailVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        temporaryStorageService.deleteEmailVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(
            @RequestBody Map<String, String> tokenRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String refreshToken = tokenRequest.get("refreshToken");

            // 리프레시 토큰 유효성 검증
            if (jwtTokenUtil.validateToken(refreshToken)) {
                String loginId = jwtTokenUtil.getUserIdFromToken(refreshToken);

                // 리프레시 토큰 삭제
                refreshTokenService.deleteRefreshToken(loginId);

                // FCM 토큰 삭제
                notificationService.deleteUserToken(Integer.parseInt(loginId));

                // 세션 무효화
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    new SecurityContextLogoutHandler().logout(request, response, auth);
                }

                log.info("사용자 ID: {}에 대한 로그아웃 처리가 완료되었습니다.", loginId);

                return ResponseEntity.ok("로그아웃 성공 및 토큰 삭제 완료");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            log.error("사용자 로그아웃 처리 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 실패: " + e.getMessage());
        }
    }
}
