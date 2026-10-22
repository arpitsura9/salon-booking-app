package com.arpit.mapper;

import com.arpit.payload.dto.BookingDTO;
import com.arpit.modal.Booking;
import com.arpit.payload.dto.SalonDTO;
import com.arpit.payload.dto.ServiceDTO;
import com.arpit.payload.dto.UserDTO;

import java.util.List;
import java.util.Set;

public class BookingMapper {

    public static BookingDTO toDTO(Booking booking, Set<ServiceDTO> services, SalonDTO salon, UserDTO user){
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setServiceIds(booking.getServiceIds());
        bookingDTO.setTotalPrice(booking.getTotalPrice());

        bookingDTO.setSalon(salon);

        bookingDTO.setServices(services);
        bookingDTO.setUser(user);

        return bookingDTO;
    }
}
