package com.project.zzimccong.service.user;


import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;

public interface UserService {
    User registerUser(UserDTO userDTO);
    User authenticate(String loginId, String password); // 수정된 부분
    boolean isEmailExists(String email);
    boolean isLoginIdExists(String loginId);
    User getUserById(String loginId); // 수정된 부분
}
