package com.example.clothes_api.services.impl;

import com.example.clothes_api.entity.Account;
import com.example.clothes_api.entity.Notification;
import com.example.clothes_api.exception.ResourceNotFoundException;
import com.example.clothes_api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private AccountService accountService;



    public void saveNotification(String email ,String message) {
        Notification notification=new Notification();
        notification.setEmail(email);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(String email) {
        return notificationRepository.findAllByEmail(email);
    }

    public void sendNotificationToUser(String email){
        for(Notification notification:getNotifications(email)){
            if(!notification.isSent()){
                notification.setSent(true);
                simpMessagingTemplate.convertAndSendToUser(email,"/queue/specific-user",notification.getMessage());
                notificationRepository.save(notification);
                System.out.println("Message : "+notification.getMessage());
            }
        }
    }
}
