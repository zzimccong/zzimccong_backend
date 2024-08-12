package com.project.zzimccong.service.user;


import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;

import java.util.List;

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

    User FindById(Integer id);



}
