package com.example.hotelbooking.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenityResponseDto {
    private Long id;
    private String name;
    private String description;
}
