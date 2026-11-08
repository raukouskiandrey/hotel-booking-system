package com.example.hotelbooking.dto.request;

import com.example.hotelbooking.model.enums.RoomType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequestDto {

    @NotBlank(message = "Номер комнаты обязателен")
    private String roomNumber;

    @NotNull(message = "Цена за ночь обязательна")
    @Positive(message = "Цена должна быть больше нуля")
    private Double pricePerNight;

    @NotNull(message = "Вместимость обязательна")
    @Min(value = 1, message = "Вместимость минимум 1 человек")
    private Integer capacity;

    @NotNull(message = "Тип номера обязателен")
    private RoomType type;

    @NotNull(message = "ID отеля обязателен")
    private Long hotelId;
}
