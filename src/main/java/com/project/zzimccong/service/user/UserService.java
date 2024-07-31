package com.project.zzimccong.service.user;


import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;

public interface UserService {
    User registerUser(UserDTO userDTO);
    User authenticate(String loginId, String password);
    boolean isEmailExists(String email);
    boolean isLoginIdExists(String loginId);
    User getUserById(String loginId);
    void sendSmsVerification(String phoneNum);
    boolean verifySmsCode(String phoneNum, String verificationCode);

    User updateUser(UserDTO userDTO);
}
