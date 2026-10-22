package com.arpit.service.impl;

import com.arpit.mapper.NotificationMapper;
import com.arpit.modal.Notification;
import com.arpit.payload.dto.BookingDTO;
import com.arpit.payload.dto.NotificationDTO;
import com.arpit.repository.NotificationRepository;
import com.arpit.service.NotificationService;
import com.arpit.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final BookingFeignClient bookingFeignClient;

    private final RealTimeCommunicationsService realTimeCommunicationsService;
    @Override
    public NotificationDTO createNotification(Notification notification) throws Exception {

        Notification savedNotification = notificationRepository.save(notification);

        BookingDTO bookingDTO = bookingFeignClient.getBookingById(
                savedNotification.getBookingId()).getBody();

        NotificationDTO notificationDTO = NotificationMapper.toDTO(
                savedNotification,
                bookingDTO
        );

        realTimeCommunicationsService.sendNotification(notificationDTO);

        return notificationDTO;
    }

    @Override
    public List<Notification> getAllNotificationByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getAllNotificationBySalonId(Long salonId) {
        return notificationRepository.findBySalonId(salonId);
    }

    @Override
    public Notification markNotificationAsRead(Long notificationId) throws Exception {
        return notificationRepository.findById(notificationId).map(
                notification -> {
                    notification.setIsRead(true);
                    return notificationRepository.save(notification);
                }
        ).orElseThrow(()-> new Exception("Notification not found"));
    }
}
