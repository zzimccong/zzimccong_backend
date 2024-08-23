package com.project.zzimccong.model.dto.notification;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class NotificationRequest {

    @NotEmpty(message = "Token is required")
    private String token;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Message is required")
    private String message;
}