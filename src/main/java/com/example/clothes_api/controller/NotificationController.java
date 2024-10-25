package com.example.clothes_api.controller;

import com.example.clothes_api.dto.NotificationRequest;
import com.example.clothes_api.services.impl.JWTService;
import com.example.clothes_api.services.impl.NotificationService;
import com.example.clothes_api.services.impl.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/push-notification")
@RequiredArgsConstructor
public class NotificationController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;
    private final RedisService redisService;
    private final JWTService jwtService;

    @MessageMapping("/send-notification")
    public void sendNotification(@Payload NotificationRequest request) throws Exception {
        //neu user on thi gui con khong thi luu vao db
        if(redisService.isUserConnected(request.getEmail())) {
            simpMessagingTemplate.convertAndSendToUser(request.getEmail(), "/queue/specific-user", request.getMessage());
            notificationService.sendNotificationToUser(request.getEmail());
            System.out.println("Message : "+request.getMessage());
        }
        else {
            notificationService.saveNotification(request.getEmail(), request.getMessage());
        }
    }

    @MessageMapping("/connect")
    public void connect(String token) {
        // Gọi phương thức để lưu trạng thái kết nối của người dùng
        String email = jwtService.extractEmail(token);
        redisService.saveUserConnection(email);
        // Thực hiện các hành động khác nếu cần
        System.out.println("User connected: " + email);
    }

//    @MessageMapping("/secured/room")
//    public void sendSpecific(
//            String message,
//            @Header("Authorization") String token
//    ){
//        String email = jwtService.extractEmail(token);
//        simpMessagingTemplate.convertAndSendToUser(email, "/secured/user/queue/specific-user", message);
//    }
//
//    @PostMapping("/save-connection")
//    public void saveConnection(String email) {
//        redisService.saveUserConnection(email);
//    }
}
