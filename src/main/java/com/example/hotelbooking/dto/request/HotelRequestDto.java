package com.example.hotelbooking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequestDto {

    @NotBlank(message = "Название отеля обязательно")
    @Size(min = 2, max = 100, message = "Название отеля должно быть от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Город обязателен")
    private String city;

    @NotBlank(message = "Адрес обязателен")
    private String address;

    @NotNull(message = "Рейтинг обязателен")
    @DecimalMin(value = "1.0", message = "Минимальный рейтинг 1.0")
    @DecimalMax(value = "5.0", message = "Максимальный рейтинг 5.0")
    private Double rating;

    private Set<Long> amenityIds;
}
