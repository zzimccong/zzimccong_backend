package com.project.zzimccong.repository.user;

import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByEmail(String email);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<User> findByLoginIdAndPassword(String loginId, String password);
    Optional<User> findByNameAndEmail(String name, String email);

    Optional<User> findFirstByRole(String role);



}
