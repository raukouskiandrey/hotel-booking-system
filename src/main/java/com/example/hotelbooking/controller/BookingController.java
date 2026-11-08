package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.request.BookingRequestDto;
import com.example.hotelbooking.dto.response.BookingResponseDto;
import com.example.hotelbooking.model.enums.BookingStatus;
import com.example.hotelbooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Управление бронированиями номеров")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "Получить список всех бронирований")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить бронирование по ID")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новое бронирование (расчет стоимости по суткам)")
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Изменить статус бронирования (CONFIRMED, CANCELLED)")
    public ResponseEntity<BookingResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить бронирование")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
