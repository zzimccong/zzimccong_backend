// UserServiceImpl.java
package com.project.zzimccong.service.user;

import com.project.zzimccong.model.dto.user.UserDTO;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
