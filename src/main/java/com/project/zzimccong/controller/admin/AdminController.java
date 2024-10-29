package com.project.zzimccong.controller.admin;

import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.service.admin.AdminService;
import com.project.zzimccong.service.corp.CorporationService;
import com.project.zzimccong.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;
    private final CorporationService corporationService;

    public AdminController(UserService userService, AdminService adminService, CorporationService corporationService) {
        this.userService = userService;
        this.adminService = adminService;
        this.corporationService = corporationService;
    }

    // 모든 일반 사용자를 조회하는 API 엔드포인트 (관리자용)
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsersForAdmin() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users); // 200 OK 상태와 함께 사용자 목록 반환
    }

    // 모든 기업 사용자를 조회하는 API 엔드포인트 (관리자용)
    @GetMapping("/corps/all")
    public ResponseEntity<List<Corporation>> getAllCorps() {
        List<Corporation> corps = adminService.getAllCorps();
        return ResponseEntity.ok(corps); // 200 OK 상태와 함께 기업 목록 반환
    }

    // 일반 사용자 상세 정보 조회 엔드포인트 (관리자용)
    @GetMapping("/edit-user/{id}")
    public ResponseEntity<User> getUserByIdForAdmin(@PathVariable Integer id) {
        try {
            User user = adminService.getFindByUserId(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 기업 사용자 상세 정보 조회 엔드포인트 (관리자용)
    @GetMapping("/edit-corp/{id}")
    public ResponseEntity<Corporation> getCorpByIdForAdmin(@PathVariable Integer id) {
        try {
            Corporation corp = adminService.getFindByCorpId(id);
            if (corp != null) {
                return ResponseEntity.ok(corp);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 일반 사용자 정보 업데이트 엔드포인트 (관리자용)
    @PutMapping("/edit-user/{id}")
    public ResponseEntity<String> adminUpdateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        try {
            userDTO.setId(id);
            userService.updateUser(userDTO);
            return ResponseEntity.ok("업데이트 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트 실패: " + e.getMessage());
        }
    }

    // 기업 사용자 정보 업데이트 엔드포인트 (관리자용)
    @PutMapping("/edit-corp/{id}")
    public ResponseEntity<String> adminUpdateCorp(@PathVariable Integer id, @RequestBody CorporationDTO corpDTO) {
        try {
            corpDTO.setId(id);
            corporationService.updateCorporation(corpDTO);
            return ResponseEntity.ok("업데이트 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트 실패: " + e.getMessage());
        }
    }

    // 일반 사용자 삭제 엔드포인트 (관리자용)
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        try {
            String adminPassword = request.get("password");
            adminService.deleteUser(id, adminPassword);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패: " + e.getMessage());
        }
    }

    // 기업 사용자 삭제 엔드포인트 (관리자용)
    @DeleteMapping("/delete-corp/{id}")
    public ResponseEntity<String> deleteCorp(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        try {
            String adminPassword = request.get("password");
            adminService.deleteCorp(id, adminPassword);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패: " + e.getMessage());
        }
    }


}
