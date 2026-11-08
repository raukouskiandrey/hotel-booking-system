package com.example.hotelbooking.mapper;

import com.example.hotelbooking.dto.request.AmenityRequestDto;
import com.example.hotelbooking.dto.response.AmenityResponseDto;
import com.example.hotelbooking.model.Amenity;
import org.springframework.stereotype.Component;

@Component
public class AmenityMapper {

    public Amenity toEntity(AmenityRequestDto dto) {
        if (dto == null) return null;
        return Amenity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public AmenityResponseDto toDto(Amenity entity) {
        if (entity == null) return null;
        return AmenityResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
