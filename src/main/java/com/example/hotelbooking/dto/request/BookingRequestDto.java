package com.example.hotelbooking.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDto {

    @NotNull(message = "Дата заезда обязательна")
    @FutureOrPresent(message = "Дата заезда не может быть в прошлом")
    private LocalDate checkInDate;

    @NotNull(message = "Дата выезда обязательна")
    private LocalDate checkOutDate;

    @NotNull(message = "ID номера обязателен")
    private Long roomId;

    @NotNull(message = "ID гостя обязателен")
    private Long guestId;
}
