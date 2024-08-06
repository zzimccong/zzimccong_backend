package com.project.zzimccong.service.admin;

import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{
    private final CorporationRepository corpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자 주입을 통해 필요한 리포지토리와 패스워드 인코더를 주입받음
    public AdminServiceImpl(CorporationRepository corpRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.corpRepository = corpRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 모든 사용자 정보를 반환하는 메서드
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(); // JpaRepository의 findAll 메소드 사용
    }

    // ID를 기준으로 사용자를 찾는 메서드
    @Override
    public User getFindByUserId(Integer id) {
        return userRepository.findById(id).orElse(null); // 사용자 ID로 사용자 정보를 찾음, 없으면 null 반환
    }

    // 모든 기업 정보를 반환하는 메서드
    @Override
    public List<Corporation> getAllCorps() {
        return corpRepository.findAll(); // JpaRepository의 findAll 메소드 사용
    }

    // ID를 기준으로 기업을 찾는 메서드
    @Override
    public Corporation getFindByCorpId(Integer id) {
        return corpRepository.findById(id).orElse(null); // 기업 ID로 기업 정보를 찾음, 없으면 null 반환
    }

    // 사용자 삭제 메서드
    @Override
    public void deleteUser(Integer userId, String adminPassword) throws Exception {
        // role이 'admin'인 관리자를 찾음
        User admin = userRepository.findFirstByRole("admin")
                .orElseThrow(() -> new Exception("Admin not found"));

        // 관리자 비밀번호 검증
        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            throw new Exception("Invalid admin credentials");
        }

        // 일반 회원 삭제
        userRepository.deleteById(userId);
    }
}
