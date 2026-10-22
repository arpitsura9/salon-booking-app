package com.arpit.service.impl;

import com.arpit.domain.BookingStatus.BookingStatus;
import com.arpit.modal.Booking;
import com.arpit.payload.dto.BookingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingChartServiceImpl {

    public List<Map<String, Object>> generateEarningsChartData(List<Booking> bookings) {
        Map<String,Integer> earningsByDay = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getStartTime().toLocalDate().toString(),
                        Collectors.summingInt(Booking::getTotalPrice)
                ));
        return convertToChartData(earningsByDay, "daily","earnings");
    }

    private <T> List<Map<String, Object>> convertToChartData(Map<String, T>groupedData, String period, String dataKey) {
        List<Map<String, Object>> chartData = new ArrayList<>();

        groupedData.forEach((date, value) -> {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put(period, date);
            dataPoint.put(dataKey, value);
            chartData.add(dataPoint);
        });

        chartData.sort(Comparator.comparing(dp -> dp.get(period).toString()));
        return chartData;
    }


    public List<Map<String, Object>> generateBookingCountChartData(List<Booking> bookings) {
        Map<String,Long> countsByDay = bookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED)
                .collect(Collectors.groupingBy(
                        booking -> booking.getStartTime().toLocalDate().toString(),
                        Collectors.counting()
                ));
        return convertToChartData(countsByDay, "daily","earnings");
    }
}
