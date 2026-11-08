package com.example.hotelbooking.mapper;

import com.example.hotelbooking.dto.request.GuestRequestDto;
import com.example.hotelbooking.dto.response.GuestResponseDto;
import com.example.hotelbooking.model.Guest;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {

    public Guest toEntity(GuestRequestDto dto) {
        if (dto == null) return null;
        return Guest.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public GuestResponseDto toDto(Guest entity) {
        if (entity == null) return null;
        return GuestResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
