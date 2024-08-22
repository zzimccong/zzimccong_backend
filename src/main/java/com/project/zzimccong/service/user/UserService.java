package com.project.zzimccong.service.user;


import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;

import java.time.LocalDate;

public interface UserService {

    User registerUser(UserDTO userDTO);
    User authenticate(String loginId, String password);
    boolean isEmailExists(String email);
    boolean isLoginIdExists(String loginId);
    User getUserById(String loginId);
    boolean isPhoneExists(String phone);

    void sendSmsVerification(String phoneNum);
    boolean verifySmsCode(String phoneNum, String verificationCode);

    User updateUser(UserDTO userDTO);
    void changePassword(String loginId, String oldPassword, String newPassword);

    boolean deleteUser(String loginId, String password);

    User getUserByNameAndEmail(String name, String email);
    void sendTemporaryPassword(String loginId, String email);

    User findById(Integer id);
    User createManagerUser(String loginId, String password, String name, LocalDate birth, String email, String phone) throws Exception;

    // 카카오
    User registerUserWithoutPassword(UserDTO userDTO);
}
