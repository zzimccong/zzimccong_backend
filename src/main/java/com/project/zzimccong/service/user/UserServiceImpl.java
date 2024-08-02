// UserServiceImpl.java
package com.project.zzimccong.service.user;

import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.service.email.EmailVerificationService;
import com.project.zzimccong.service.redis.TemporaryStorageService;
import com.project.zzimccong.sms.SmsUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsUtil smsUtil;
    private final TemporaryStorageService temporaryStorageService;
    private final EmailVerificationService emailVerificationService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, SmsUtil smsUtil, TemporaryStorageService temporaryStorageService, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.smsUtil = smsUtil;
        this.temporaryStorageService = temporaryStorageService;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setBirth(LocalDate.parse(userDTO.getBirth()));
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());

        return userRepository.save(user);
    }

    @Override
    public User authenticate(String loginId, String password) {
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isLoginIdExists(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Override
    public User getUserById(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + loginId));
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public void sendSmsVerification(String phoneNum) {
        String verificationCode = generateVerificationCode();
        smsUtil.sendOne(phoneNum, verificationCode);
        temporaryStorageService.saveSMSVerificationCode(phoneNum, verificationCode);
    }

    @Override
    public boolean verifySmsCode(String phoneNum, String verificationCode) {
        return temporaryStorageService.verifySMSCode(phoneNum, verificationCode);
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(8999) + 1000; // 1000 ~ 9999 범위의 숫자 생성
        return String.valueOf(code);
    }

    @Override
    public User updateUser(UserDTO userDTO) {
        User user = userRepository.findByLoginId(userDTO.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getBirth() != null) {
            user.setBirth(LocalDate.parse(userDTO.getBirth()));
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null) {
            user.setPhone(userDTO.getPhone());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public void changePassword(String loginId, String oldPassword, String newPassword) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean deleteUser(String loginId, String password) {
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public User getUserByNameAndEmail(String name, String email) {
        return userRepository.findByNameAndEmail(name, email).orElse(null);
    }

    @Override
    public void sendTemporaryPassword(String loginId, String email) {
        emailVerificationService.sendTemporaryPassword(null, loginId, email);
    }
}
