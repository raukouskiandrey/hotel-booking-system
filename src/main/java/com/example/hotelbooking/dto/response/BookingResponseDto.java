package com.example.hotelbooking.dto.response;

import com.example.hotelbooking.model.enums.BookingStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalPrice;
    private BookingStatus status;
    private Long roomId;
    private String roomNumber;
    private Long guestId;
    private String guestName;
}
