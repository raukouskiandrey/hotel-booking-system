package com.example.hotelbooking.dto.response;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponseDto {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Double rating;
    private List<RoomResponseDto> rooms;
    private Set<AmenityResponseDto> amenities;
}
