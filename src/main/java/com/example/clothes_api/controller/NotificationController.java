package com.example.clothes_api.controller;

import com.example.clothes_api.services.impl.NotificationService;
import com.example.clothes_api.services.impl.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/push-notification")
@RequiredArgsConstructor
public class NotificationController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;
    private final RedisService redisService;


    @MessageMapping("/send-notification")
    public void sendNotification(String message, String email) {
        //neu user on thi gui con khong thi luu vao db
        if(redisService.isUserConnected(email)) {
            simpMessagingTemplate.convertAndSendToUser(email, "/topic/notification", message);
            notificationService.sendNotificationToUser(email);
        }
        else {
            notificationService.saveNotification(email, message);
        }
    }
}
