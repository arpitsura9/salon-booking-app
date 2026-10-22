package com.arpit.controller;

import com.arpit.domain.BookingStatus.BookingStatus;
import com.arpit.domain.BookingStatus.PaymentMethod;
import com.arpit.mapper.BookingMapper;
import com.arpit.modal.Booking;
import com.arpit.modal.SalonReport;
import com.arpit.payload.dto.*;
import com.arpit.service.BookingService;
import com.arpit.service.client.PaymentFeignClient;
import com.arpit.service.client.SalonFeignClient;
import com.arpit.service.client.ServiceOfferingFeignClients;
import com.arpit.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final SalonFeignClient salonFeignClient;
    private final UserFeignClient userFeignClient;
    private final ServiceOfferingFeignClients serviceOfferingFeignClients;
    private final PaymentFeignClient paymentFeignClient;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createBooking(
            @RequestParam Long salonId,
            @RequestParam PaymentMethod paymentMethod,
            @RequestBody BookingRequest bookingRequest,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDTO user = userFeignClient.getUserProfile(jwt).getBody();

        SalonDTO salon = salonFeignClient.getSalonById(salonId).getBody();



        Set<ServiceDTO> serviceDTOSet = serviceOfferingFeignClients.getServicesByIds(bookingRequest.getServiceIds()).getBody();

        if(serviceDTOSet.isEmpty()){
            throw new Exception("Service not found...");
        }

        Booking booking = bookingService.createBooking(bookingRequest,
                user,salon, serviceDTOSet);


        Set<ServiceDTO> services = serviceOfferingFeignClients.getServicesByIds(booking.getServiceIds()).getBody();
        UserDTO customer  = userFeignClient.getUserById(booking.getCustomerId()).getBody();

        BookingDTO bookingDTO = BookingMapper.toDTO(booking,services,salon,customer);
        PaymentLinkResponse res = paymentFeignClient.createPaymentLink(
                bookingDTO,
                paymentMethod,
                jwt
        ).getBody();

        return ResponseEntity.ok(res);
    }


    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(
        @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserDTO user = userFeignClient.getUserProfile(jwt).getBody();
        if(user==null || user.getId()==null){
            throw new Exception("user not found from jwt...");
        }

        List<Booking> bookings = bookingService.getBookingsByCustomer(user.getId());


        return ResponseEntity.ok(getBookingDTOs(bookings));
    }


    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(
        @RequestHeader("Authorization") String jwt
    )throws Exception{

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();

        List<Booking> bookings = bookingService.getBookingsBySalon(salonDTO.getId());


        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {

        return bookings.stream()
                .map(booking->{
                    Set<ServiceDTO> services = serviceOfferingFeignClients.
                            getServicesByIds(booking.getServiceIds()).getBody();
                    SalonDTO salon;
                    UserDTO user;
                    try {
                         salon = salonFeignClient.getSalonById(booking.getSalonId()).getBody();
                         user  = userFeignClient.getUserById(booking.getCustomerId()).getBody();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return BookingMapper.toDTO(booking,services, salon, user);

                }).collect(Collectors.toSet());
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingsById(
            @PathVariable Long bookingId
    ) throws Exception {

        Booking booking = bookingService.getBookingById(bookingId);

        Set<ServiceDTO> services = serviceOfferingFeignClients.getServicesByIds(booking.getServiceIds()).getBody();
        UserDTO customer  = userFeignClient.getUserById(booking.getCustomerId()).getBody();
        SalonDTO salon = salonFeignClient.getSalonById(booking.getSalonId()).getBody();
        return ResponseEntity.ok(BookingMapper.toDTO(booking,services, salon, customer));
    }


    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status
    ) throws Exception {

        Booking booking = bookingService.updateBooking(bookingId,status);

        Set<ServiceDTO> services = serviceOfferingFeignClients.getServicesByIds(booking.getServiceIds()).getBody();
        UserDTO customer  = userFeignClient.getUserById(booking.getCustomerId()).getBody();
        SalonDTO salon = salonFeignClient.getSalonById(booking.getSalonId()).getBody();
        return ResponseEntity.ok(BookingMapper.toDTO(booking,services, salon, customer));

    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookedSlot(
            @PathVariable Long salonId,
            @RequestParam(required = false) LocalDate date
    ) throws Exception {

        List<Booking> bookings = bookingService.getBookingByDate(date,salonId);

        List<BookingSlotDTO> slotsDTOs = bookings.stream()
                .map(booking -> {
                    BookingSlotDTO slotDTO = new BookingSlotDTO();
                    slotDTO.setStartTime(booking.getStartTime());
                    slotDTO.setEndTime(booking.getEndTime());
                    return slotDTO;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(slotsDTOs);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(
        @RequestHeader("Authorization") String jwt
    ) throws Exception {

        SalonDTO salonDTO = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        SalonReport report = bookingService.getSalonReport(salonDTO.getId());


        return ResponseEntity.ok(report);
    }

}
