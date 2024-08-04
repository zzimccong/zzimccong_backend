package com.project.zzimccong.controller.admin;

import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.service.corp.CorporationService;
import com.project.zzimccong.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final CorporationService corporationService;

    public AdminController(UserService userService, CorporationService corporationService) {
        this.userService = userService;
        this.corporationService = corporationService;
    }

    // 모든 일반 사용자를 조회하는 API 엔드포인트 (관리자용)
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsersForAdmin() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // 200 OK 상태와 함께 사용자 목록 반환
    }

    // 모든 기업 사용자를 조회하는 API 엔드포인트 (관리자용)
    @GetMapping("/corps/all")
    public ResponseEntity<List<Corporation>> getAllCorps() {
        List<Corporation> corp = corporationService.getAllCorps();
        return ResponseEntity.ok(corp); // 200 OK 상태와 함께 사용자 목록 반환
    }


}
