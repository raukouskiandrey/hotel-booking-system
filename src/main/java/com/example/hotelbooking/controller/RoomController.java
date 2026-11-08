package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.request.RoomRequestDto;
import com.example.hotelbooking.dto.response.RoomResponseDto;
import com.example.hotelbooking.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Управление номерами отелей (Лабораторные 1, 2, 5)")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Получить все номера отеля по hotelId")
    public ResponseEntity<List<RoomResponseDto>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить номер по ID")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping
    @Operation(summary = "Создать один номер")
    public ResponseEntity<RoomResponseDto> createRoom(@Valid @RequestBody RoomRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(dto));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk-операция: массовое добавление номеров (Лабораторная 5)")
    public ResponseEntity<List<RoomResponseDto>> createRoomsBulk(@Valid @RequestBody List<RoomRequestDto> dtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoomsBulk(dtos));
    }

    @PostMapping("/bulk-demo-no-transaction")
    @Operation(summary = "Демонстрация сохранения БЕЗ @Transactional (частичное сохранение при сбое) (Лабораторная 2, 5)")
    public ResponseEntity<List<RoomResponseDto>> bulkNoTransactionDemo(@Valid @RequestBody List<RoomRequestDto> dtos) {
        return ResponseEntity.ok(roomService.bulkSaveWithoutTransactionalDemo(dtos));
    }

    @PostMapping("/bulk-demo-with-transaction")
    @Operation(summary = "Демонстрация сохранения С @Transactional (полный откат при сбое) (Лабораторная 2, 5)")
    public ResponseEntity<List<RoomResponseDto>> bulkWithTransactionDemo(@Valid @RequestBody List<RoomRequestDto> dtos) {
        return ResponseEntity.ok(roomService.bulkSaveWithTransactionalDemo(dtos));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить номер")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
