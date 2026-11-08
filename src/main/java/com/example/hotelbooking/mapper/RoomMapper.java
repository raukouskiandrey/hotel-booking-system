package com.example.hotelbooking.mapper;

import com.example.hotelbooking.dto.request.RoomRequestDto;
import com.example.hotelbooking.dto.response.RoomResponseDto;
import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toEntity(RoomRequestDto dto, Hotel hotel) {
        if (dto == null) return null;
        return Room.builder()
                .roomNumber(dto.getRoomNumber())
                .pricePerNight(dto.getPricePerNight())
                .capacity(dto.getCapacity())
                .type(dto.getType())
                .hotel(hotel)
                .build();
    }

    public RoomResponseDto toDto(Room entity) {
        if (entity == null) return null;
        return RoomResponseDto.builder()
                .id(entity.getId())
                .roomNumber(entity.getRoomNumber())
                .pricePerNight(entity.getPricePerNight())
                .capacity(entity.getCapacity())
                .type(entity.getType())
                .hotelId(entity.getHotel() != null ? entity.getHotel().getId() : null)
                .hotelName(entity.getHotel() != null ? entity.getHotel().getName() : null)
                .build();
    }
}
