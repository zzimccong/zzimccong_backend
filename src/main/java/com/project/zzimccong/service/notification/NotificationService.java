package com.project.zzimccong.service.notification;

public interface NotificationService {
    void saveUserToken(Integer userId, String token);
    void saveCorpToken(Integer corpId, String token);

    String getUserToken(Integer userId);

    String getCorpToken(Integer corpId);

    void deleteUserToken(Integer userId);  // 사용자 토큰 삭제 메서드
    void deleteCorpToken(Integer corpId);  // 기업 토큰 삭제 메서드


}
