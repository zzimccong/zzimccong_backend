package com.project.zzimccong.service.notification;

public interface NotificationService {
    void saveUserToken(Integer userId, String token);
    void saveCorpToken(Integer corpId, String token);

    String getUserToken(Integer userId);

    String getCorpToken(Integer corpId);

}
