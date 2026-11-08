package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.request.GuestRequestDto;
import com.example.hotelbooking.dto.response.GuestResponseDto;
import com.example.hotelbooking.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
@Tag(name = "Guests", description = "Управление гостями отелей")
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    @Operation(summary = "Получить список всех гостей")
    public ResponseEntity<List<GuestResponseDto>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить гостя по ID")
    public ResponseEntity<GuestResponseDto> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @PostMapping
    @Operation(summary = "Зарегистрировать нового гостя")
    public ResponseEntity<GuestResponseDto> createGuest(@Valid @RequestBody GuestRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.createGuest(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные гостя")
    public ResponseEntity<GuestResponseDto> updateGuest(@PathVariable Long id, @Valid @RequestBody GuestRequestDto dto) {
        return ResponseEntity.ok(guestService.updateGuest(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить гостя")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }
}
