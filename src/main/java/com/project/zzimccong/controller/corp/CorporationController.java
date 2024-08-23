package com.project.zzimccong.controller.corp;


import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.dto.email.EmailDTO;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.corp.CorporationService;
import com.project.zzimccong.service.redis.RefreshTokenService;
import com.project.zzimccong.service.redis.TemporaryStorageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.service.email.EmailVerificationService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/corporations")
public class CorporationController {

    private final CorporationService corporationService;
    private final EmailVerificationService emailVerificationService;
    private final JwtTokenUtil jwtTokenUtil;
    private final TemporaryStorageService temporaryStorageService;
    private final RefreshTokenService refreshTokenService;
    public CorporationController(CorporationService corporationService, EmailVerificationService emailVerificationService, JwtTokenUtil jwtTokenUtil, TemporaryStorageService temporaryStorageService, RefreshTokenService refreshTokenService) {
        this.corporationService = corporationService;
        this.emailVerificationService = emailVerificationService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.temporaryStorageService = temporaryStorageService;
        this.refreshTokenService = refreshTokenService;
    }

    // 회사 등록 엔드포인트
    @PostMapping("/corp-register")
    public ResponseEntity<String> registerCorporation(@RequestBody CorporationDTO corporationDTO) {
        if (corporationService.isCorpEmailExists(corporationDTO.getCorpEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다");
        }
        if (corporationService.isCorpIdExists(corporationDTO.getCorpId())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 ID입니다");
        }
        try {
            Corporation corporation = corporationService.registerCorporation(corporationDTO);
            return ResponseEntity.ok("등록 성공. 인증 이메일이 다음 주소로 발송되었습니다: " + corporation.getCorpEmail());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("등록 실패: " + e.getMessage());
        }
    }

    // 이메일 인증 코드 검증 엔드포인트
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setCorpEmail(email);
        emailDTO.setVerificationCode(code);

        try {
            boolean isVerified = emailVerificationService.verifyCode(emailDTO);
            if (isVerified) {
                return ResponseEntity.ok("이메일 인증 성공.");
            } else {
                return ResponseEntity.badRequest().body("잘못된 인증 코드.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이메일 인증 오류: " + e.getMessage());
        }
    }

    // 이메일 인증 코드 재전송 엔드포인트
    @PostMapping("/resend-email")
    public ResponseEntity<String> resendEmailVerificationCode(@RequestParam String email) {
        try {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setCorpEmail(email);
            emailVerificationService.sendVerificationEmail(emailDTO);
            return ResponseEntity.ok("인증 이메일이 재전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 이메일 재전송 실패: " + e.getMessage());
        }
    }

    // 이메일 인증 코드 삭제 엔드포인트
    @PostMapping("/delete-email")
    public ResponseEntity<Void> deleteEmailVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        temporaryStorageService.deleteEmailVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    // 로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody CorporationDTO corporationDTO) {
        try {
            Corporation corporation = corporationService.authenticate(corporationDTO.getCorpId(), corporationDTO.getPassword());
            if (corporation != null) {
                String token = jwtTokenUtil.generateToken(corporation.getCorpId(), "corp");
                String refreshToken = jwtTokenUtil.generateRefreshToken(corporation.getCorpId());
                refreshTokenService.saveRefreshToken(corporation.getCorpId(), refreshToken); // 리프레시 토큰 저장
                Map<String, Object> response = getStringObjectMap(corporation, token, refreshToken);
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
            String corpId = jwtTokenUtil.getUserIdFromToken(refreshToken);
            String storedToken = refreshTokenService.getRefreshToken(corpId);
            if (refreshToken.equals(storedToken)) {
                String newAccessToken = jwtTokenUtil.generateToken(corpId, "corp");
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

    @PostMapping("/logout")
    public ResponseEntity<?> logoutCorporation(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        if (jwtTokenUtil.validateToken(refreshToken)) {
            String corpId = jwtTokenUtil.getUserIdFromToken(refreshToken);
            refreshTokenService.deleteRefreshToken(corpId); // Redis에서 리프레시 토큰 삭제
            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 토큰입니다.");
        }
    }

    @NotNull
    private Map<String, Object> getStringObjectMap(Corporation corporation, String token, String refreshToken) {
        CorporationDTO responseCorpDTO = new CorporationDTO(
                corporation.getId(),
                corporation.getCorpName(),
                corporation.getCorpDept(),
                corporation.getCorpId(),
                null, // 비밀번호는 반환되지 않음
                corporation.getCorpEmail(),
                corporation.getCorpAddress(),
                corporation.isEmailVerified(),
                corporation.getRole()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("refreshToken", refreshToken);
        response.put("user", responseCorpDTO); // CorporationDTO로 응답
        return response;
    }


    // 회사 ID 존재 여부 체크 엔드포인트
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkCorpIdExists(@RequestParam String corpId) {
        boolean exists = corporationService.isCorpIdExists(corpId);
        return ResponseEntity.ok(exists);
    }

    // 회사 정보 조회 엔드포인트
    @GetMapping("/{corpId}")
    public ResponseEntity<Corporation> getCorporationById(@PathVariable String corpId) {
        try {
            Corporation corporation = corporationService.getCorporationById(corpId);
            return ResponseEntity.ok(corporation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 회사 정보 업데이트 엔드포인트
    @PutMapping("/{corpId}")
    public ResponseEntity<String> updateCorporation(@PathVariable String corpId, @RequestBody CorporationDTO corporationDTO) {
        try {
            corporationDTO.setCorpId(corpId);
            corporationService.updateCorporation(corporationDTO);
            return ResponseEntity.ok("업데이트 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 비밀번호 변경 엔드포인트
    @PutMapping("/{corpId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable String corpId,
            @RequestBody Map<String, String> passwordMap) {
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");

        try {
            corporationService.changePassword(corpId, oldPassword, newPassword);
            return ResponseEntity.ok("비밀번호 변경 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 회사 계정 삭제 엔드포인트
    @PostMapping("/{corpId}/delete")
    public ResponseEntity<String> deleteCorporation(@PathVariable String corpId, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        try {
            boolean isDeleted = corporationService.deleteCorporation(corpId, password);
            if (isDeleted) {
                return ResponseEntity.ok("계정 삭제 성공");
            } else {
                return ResponseEntity.status(401).body("잘못된 비밀번호");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("계정 삭제 실패");
        }
    }

    // 회사 ID 찾기 엔드포인트
    @PostMapping("/find-id")
    public ResponseEntity<?> findCorpId(@RequestBody CorporationDTO corporationDTO) {
        try {
            Corporation corp = corporationService.getCorporationByNameAndEmail(corporationDTO.getCorpName(), corporationDTO.getCorpEmail());
            if (corp != null) {
                return ResponseEntity.ok(corp.getCorpId());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회사를 찾을 수 없습니다");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회사 ID 찾기 실패: " + e.getMessage());
        }
    }

    // 임시 비밀번호 전송 엔드포인트
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody CorporationDTO corporationDTO) {
        try {
            corporationService.sendTemporaryPassword(corporationDTO.getCorpId(), corporationDTO.getCorpEmail());
            return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("임시 비밀번호 전송 실패: " + e.getMessage());
        }
    }
}
