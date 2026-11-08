package com.example.hotelbooking.dto.response;

import com.example.hotelbooking.model.enums.RoomType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDto {
    private Long id;
    private String roomNumber;
    private Double pricePerNight;
    private Integer capacity;
    private RoomType type;
    private Long hotelId;
    private String hotelName;
}
