package com.project.zzimccong.controller.user;

import com.project.zzimccong.model.dto.sms.SmsVerificationDTO;
import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.redis.TemporaryStorageService;
import com.project.zzimccong.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    // 필드 선언: TemporaryStorageService, UserService, JwtTokenUtil
    private final TemporaryStorageService temporaryStorageService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    // 생성자를 통해 서비스와 JWT 유틸리티 주입
    public UserController(TemporaryStorageService temporaryStorageService, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.temporaryStorageService = temporaryStorageService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // 사용자 등록 엔드포인트
    @PostMapping("/user-register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("사용자 등록에 성공했습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 등록에 실패했습니다: " + e.getMessage());
        }
    }

    // 아이디 중복 체크 엔드포인트
    @GetMapping("/check-id")
    public ResponseEntity<?> checkLoginId(@RequestParam String loginId) {
        try {
            boolean exists = userService.isLoginIdExists(loginId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("아이디 확인 실패: " + e.getMessage());
        }
    }

    // 이메일 중복 체크 엔드포인트
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userService.isEmailExists(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 확인 실패: " + e.getMessage());
        }
    }

    // 사용자 로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.authenticate(userDTO.getLoginId(), userDTO.getPassword());
            if (user != null) {
                String token = jwtTokenUtil.generateToken(user.getLoginId(), "user");
                Map<String, Object> response = createLoginResponse(token, user);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private Map<String, Object> createLoginResponse(String token, User user) {
        UserDTO responseUserDTO = new UserDTO(
                user.getId(),
                user.getLoginId(),
                null, // 비밀번호는 반환되지 않음
                user.getName(),
                user.getBirth().toString(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", responseUserDTO); // UserDTO로 응답
        return response;
    }

    // 사용자 정보 조회 엔드포인트
    @GetMapping("/{loginId}")
    public ResponseEntity<User> getUserById(@PathVariable String loginId) {
        try {
            User user = userService.getUserById(loginId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 사용자 정보 업데이트 엔드포인트
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

    // 비밀번호 변경 엔드포인트
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

    // 사용자 계정 삭제 엔드포인트
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

    // SMS 인증 코드 전송 엔드포인트
    @PostMapping("/send-sms")
    public ResponseEntity<?> sendSmsVerification(@RequestBody SmsVerificationDTO smsVerificationDTO) {
        try {
            userService.sendSmsVerification(smsVerificationDTO.getPhone());
            return ResponseEntity.ok("SMS 전송 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SMS 전송 실패: " + e.getMessage());
        }
    }

    // SMS 인증 코드 검증 엔드포인트
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

    // 사용자 아이디 찾기 엔드포인트
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

    // 임시 비밀번호 전송 엔드포인트
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.sendTemporaryPassword(userDTO.getLoginId(), userDTO.getEmail());
            return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("임시 비밀번호 전송 실패: " + e.getMessage());
        }
    }

    // SMS 인증 코드 삭제 엔드포인트
    @PostMapping("/delete-sms")
    public ResponseEntity<Void> deleteSmsVerificationCode(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        temporaryStorageService.deleteSMSVerificationCode(phone);
        return ResponseEntity.ok().build();
    }

    // 이메일 인증 코드 삭제 엔드포인트
    @PostMapping("/delete-email")
    public ResponseEntity<Void> deleteEmailVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        temporaryStorageService.deleteEmailVerificationCode(email);
        return ResponseEntity.ok().build();
    }
}
