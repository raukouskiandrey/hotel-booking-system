package com.example.hotelbooking.mapper;

import com.example.hotelbooking.dto.request.BookingRequestDto;
import com.example.hotelbooking.dto.response.BookingResponseDto;
import com.example.hotelbooking.model.Booking;
import com.example.hotelbooking.model.Guest;
import com.example.hotelbooking.model.Room;
import com.example.hotelbooking.model.enums.BookingStatus;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequestDto dto, Room room, Guest guest) {
        if (dto == null) return null;
        long days = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        if (days <= 0) days = 1;
        double totalPrice = days * room.getPricePerNight();

        return Booking.builder()
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .room(room)
                .guest(guest)
                .build();
    }

    public BookingResponseDto toDto(Booking entity) {
        if (entity == null) return null;
        return BookingResponseDto.builder()
                .id(entity.getId())
                .checkInDate(entity.getCheckInDate())
                .checkOutDate(entity.getCheckOutDate())
                .totalPrice(entity.getTotalPrice())
                .status(entity.getStatus())
                .roomId(entity.getRoom() != null ? entity.getRoom().getId() : null)
                .roomNumber(entity.getRoom() != null ? entity.getRoom().getRoomNumber() : null)
                .guestId(entity.getGuest() != null ? entity.getGuest().getId() : null)
                .guestName(entity.getGuest() != null ? entity.getGuest().getFirstName() + " " + entity.getGuest().getLastName() : null)
                .build();
    }
}
