package com.project.zzimccong.service.fcm;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import java.util.List;

public class FcmMessage {

    public static Message makeMessage(String targetToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setClickAction("push_click")  // Android의 클릭 액션 설정
                        .build())
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setCategory("push_click")  // iOS의 카테고리 설정
                        .build())
                .build();

        return Message.builder()
                .setNotification(notification)
                .setToken(targetToken)
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .build();
    }

    public static MulticastMessage makeMessages(List<String> targetTokens, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setClickAction("push_click")  // Android의 클릭 액션 설정
                        .build())
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setCategory("push_click")  // iOS의 카테고리 설정
                        .build())
                .build();

        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(targetTokens)
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .build();
    }
}
