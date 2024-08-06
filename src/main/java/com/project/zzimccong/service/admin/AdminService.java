package com.project.zzimccong.service.admin;

import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;

import java.util.List;

public interface AdminService {

    List<User> getAllUsers();

    User getFindByUserId(Integer id);

    List<Corporation> getAllCorps();
    Corporation getFindByCorpId(Integer id);
    void deleteUser(Integer userId, String adminPassword) throws Exception;
}
