package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.request.HotelRequestDto;
import com.example.hotelbooking.dto.response.HotelResponseDto;
import com.example.hotelbooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Управление отелями (Лабораторные 1, 2, 3)")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    @Operation(summary = "Получить отели по городу и минимальному рейтингу (@RequestParam)")
    public ResponseEntity<Page<HotelResponseDto>> getHotels(
            @RequestParam(required = false, defaultValue = "Минск") String city,
            @RequestParam(required = false, defaultValue = "1.0") Double minRating,
            Pageable pageable) {
        return ResponseEntity.ok(hotelService.getHotelsByCityAndRating(city, minRating, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отель по ID (@PathVariable)")
    public ResponseEntity<HotelResponseDto> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/search-cached")
    @Operation(summary = "Сложный JPQL-запрос по городу и макс. цене номера с InMemoryCache (Лабораторная 3)")
    public ResponseEntity<List<HotelResponseDto>> searchHotelsCached(
            @RequestParam String city,
            @RequestParam Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hotelService.searchHotelsWithCache(city, maxPrice, page, size));
    }

    @GetMapping("/search-native")
    @Operation(summary = "Сложный Native SQL запрос по городу и макс. цене номера (Лабораторная 3)")
    public ResponseEntity<List<HotelResponseDto>> searchHotelsNative(
            @RequestParam String city,
            @RequestParam Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hotelService.searchHotelsNative(city, maxPrice, page, size));
    }

    @GetMapping("/demo/n-plus-one")
    @Operation(summary = "Демонстрация проблемы N+1 (смотреть в логи консоли Hibernate) (Лабораторная 2)")
    public ResponseEntity<List<String>> demoNPlusOne() {
        return ResponseEntity.ok(hotelService.demonstrateNPlusOneProblem());
    }

    @GetMapping("/demo/n-plus-one-fixed")
    @Operation(summary = "Решение проблемы N+1 через @EntityGraph (Лабораторная 2)")
    public ResponseEntity<List<String>> demoNPlusOneFixed() {
        return ResponseEntity.ok(hotelService.demonstrateNPlusOneSolution());
    }

    @PostMapping
    @Operation(summary = "Создать отель (с валидацией @Valid)")
    public ResponseEntity<HotelResponseDto> createHotel(@Valid @RequestBody HotelRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отель")
    public ResponseEntity<HotelResponseDto> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelRequestDto dto) {
        return ResponseEntity.ok(hotelService.updateHotel(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отель")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
