package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.request.AmenityRequestDto;
import com.example.hotelbooking.dto.response.AmenityResponseDto;
import com.example.hotelbooking.service.AmenityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/amenities")
@RequiredArgsConstructor
@Tag(name = "Amenities", description = "Управление услугами и удобствами отелей (ManyToMany)")
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping
    @Operation(summary = "Получить список всех удобств")
    public ResponseEntity<List<AmenityResponseDto>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.getAllAmenities());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить удобство по ID")
    public ResponseEntity<AmenityResponseDto> getAmenityById(@PathVariable Long id) {
        return ResponseEntity.ok(amenityService.getAmenityById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новое удобство")
    public ResponseEntity<AmenityResponseDto> createAmenity(@Valid @RequestBody AmenityRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(amenityService.createAmenity(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить удобство")
    public ResponseEntity<AmenityResponseDto> updateAmenity(@PathVariable Long id, @Valid @RequestBody AmenityRequestDto dto) {
        return ResponseEntity.ok(amenityService.updateAmenity(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить удобство")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}
