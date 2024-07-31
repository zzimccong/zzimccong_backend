package com.project.zzimccong.controller.user;

import com.project.zzimccong.model.dto.sms.SmsVerificationDTO;
import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/user-register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/check-id")
    public ResponseEntity<?> checkLoginId(@RequestParam String loginId) {
        try {
            boolean exists = userService.isLoginIdExists(loginId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ID check failed: " + e.getMessage());
        }
    }


    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userService.isEmailExists(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email check failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.authenticate(userDTO.getLoginId(), userDTO.getPassword());
            if (user != null) {
                String token = jwtTokenUtil.generateToken(null, user.getLoginId());
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", new UserDTO(
                        user.getLoginId(),
                        null, // password is not returned
                        user.getName(),
                        user.getBirth().toString(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole()
                ));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<User> getUserById(@PathVariable String loginId) {
        try {
            User user = userService.getUserById(loginId);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/send-sms")
    public ResponseEntity<?> sendSmsVerification(@RequestBody SmsVerificationDTO smsVerificationDTO) {
        try {
            userService.sendSmsVerification(smsVerificationDTO.getPhone());
            return ResponseEntity.ok("SMS sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send SMS: " + e.getMessage());
        }
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<?> verifySmsCode(@RequestBody SmsVerificationDTO smsVerificationDTO) {
        try {
            boolean isValid = userService.verifySmsCode(smsVerificationDTO.getPhone(), smsVerificationDTO.getVerificationCode());
            if (isValid) {
                return ResponseEntity.ok("Verification successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid verification code");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification failed: " + e.getMessage());
        }
    }

    @PutMapping("/{loginId}")
    public ResponseEntity<String> updateUser(@PathVariable String loginId, @RequestBody UserDTO userDTO) {
        try {
            userDTO.setLoginId(loginId);
            userService.updateUser(userDTO);
            return ResponseEntity.ok("Update successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{loginId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable String loginId,
            @RequestBody Map<String, String> passwordMap) {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");

        try {
            userService.changePassword(loginId, oldPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{loginId}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable String loginId, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        try {
            boolean isDeleted = userService.deleteUser(loginId, password);
            if (isDeleted) {
                return ResponseEntity.ok("Account deleted successfully");
            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete account");
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findUserId(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.getUserByNameAndEmail(userDTO.getName(), userDTO.getEmail());
            if (user != null) {
                return ResponseEntity.ok(user.getLoginId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to find user ID: " + e.getMessage());
        }
    }

    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody UserDTO userDTO) {
        try {
            userService.sendTemporaryPassword(userDTO.getLoginId(), userDTO.getEmail());
            return ResponseEntity.ok("Temporary password sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send temporary password: " + e.getMessage());
        }
    }

}
