package com.arpit.service.impl;

import com.arpit.payload.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RealTimeCommunicationsService {

    private final SimpMessagingTemplate template;

    public void sendNotification(NotificationDTO notification) {
         template.convertAndSend(
                 "/notification/user/" + notification.getUserId(),
                 notification
         );
        template.convertAndSend(
                "/notification/salon/" + notification.getSalonId(),
                notification
        );
    }
}
