package com.arpit.service;

import com.arpit.domain.BookingStatus.BookingStatus;
import com.arpit.modal.PaymentOrder;
import com.arpit.payload.dto.BookingRequest;
import com.arpit.payload.dto.SalonDTO;
import com.arpit.payload.dto.ServiceDTO;
import com.arpit.payload.dto.UserDTO;
import com.arpit.modal.Booking;
import com.arpit.modal.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {
    Booking  createBooking(BookingRequest booking,
                           UserDTO user,
                           SalonDTO salon,
                           Set<ServiceDTO> serviceDTOSet) throws Exception;

    List<Booking> getBookingsByCustomer(Long customerId);

    List<Booking> getBookingsBySalon(Long salonId);

    Booking getBookingById(Long id) throws Exception;

    Booking updateBooking(Long bookingId, BookingStatus status) throws Exception;

    List<Booking> getBookingByDate(LocalDate date, Long salonId);

    SalonReport getSalonReport(Long salonId);

    Booking bookingSuccess(PaymentOrder  order) throws Exception;
}
