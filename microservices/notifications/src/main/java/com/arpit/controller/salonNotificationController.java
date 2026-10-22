package com.arpit.controller;

import com.arpit.mapper.NotificationMapper;
import com.arpit.modal.Notification;
import com.arpit.payload.dto.BookingDTO;
import com.arpit.payload.dto.NotificationDTO;
import com.arpit.service.NotificationService;
import com.arpit.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/salon-owner")
public class salonNotificationController {

    private final NotificationService notificationService;
    private final BookingFeignClient bookingFeignClient;


    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationBySalonId(
            @PathVariable Long salonId
    ){
        List<Notification> notifications = notificationService.getAllNotificationBySalonId(salonId);

        List<NotificationDTO> notificationDTOS = notifications.stream()
                .map((notification -> {
                    BookingDTO bookingDTO = null;
                    try {
                        bookingDTO = bookingFeignClient.getBookingById(notification.getBookingId()).getBody();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return NotificationMapper.toDTO(notification, bookingDTO);
                })).collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOS);
    }
}
