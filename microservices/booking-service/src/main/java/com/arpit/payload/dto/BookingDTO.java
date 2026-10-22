package com.arpit.payload.dto;

import com.arpit.domain.BookingStatus.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class BookingDTO {

    private Long id;

    private Long salonId;

    private Long customerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Long> serviceIds;

    private BookingStatus status = BookingStatus.PENDING;

    private int totalPrice;

    private Set<ServiceDTO> services;

    private UserDTO user;

    private SalonDTO salon;
}
