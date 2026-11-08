package com.example.hotelbooking.mapper;

import com.example.hotelbooking.dto.request.HotelRequestDto;
import com.example.hotelbooking.dto.response.HotelResponseDto;
import com.example.hotelbooking.model.Hotel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HotelMapper {

    private final RoomMapper roomMapper;
    private final AmenityMapper amenityMapper;

    public Hotel toEntity(HotelRequestDto dto) {
        if (dto == null) return null;
        return Hotel.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .address(dto.getAddress())
                .rating(dto.getRating())
                .build();
    }

    public HotelResponseDto toDto(Hotel entity) {
        if (entity == null) return null;
        return HotelResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .address(entity.getAddress())
                .rating(entity.getRating())
                .rooms(entity.getRooms() != null
                        ? entity.getRooms().stream().map(roomMapper::toDto).collect(Collectors.toList())
                        : null)
                .amenities(entity.getAmenities() != null
                        ? entity.getAmenities().stream().map(amenityMapper::toDto).collect(Collectors.toSet())
                        : null)
                .build();
    }
}
