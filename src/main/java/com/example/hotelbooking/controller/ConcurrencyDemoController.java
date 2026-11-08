package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.response.AsyncTaskResponseDto;
import com.example.hotelbooking.dto.response.RaceConditionResultDto;
import com.example.hotelbooking.service.AsyncBookingService;
import com.example.hotelbooking.service.RaceConditionDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/concurrency")
@RequiredArgsConstructor
@Tag(name = "Concurrency", description = "Многопоточность: асинхронные задачи и Race Condition (Лабораторная 6)")
public class ConcurrencyDemoController {

    private final AsyncBookingService asyncBookingService;
    private final RaceConditionDemoService raceConditionDemoService;

    @PostMapping("/async-report")
    @Operation(summary = "Запустить асинхронную операцию (возвращает taskId)")
    public ResponseEntity<Map<String, String>> startAsyncReport(@RequestParam(defaultValue = "Минск") String city) {
        String taskId = asyncBookingService.startAsyncReportGeneration(city);
        return ResponseEntity.accepted().body(Map.of(
                "taskId", taskId,
                "status", "IN_PROGRESS",
                "checkStatusUrl", "/api/v1/concurrency/tasks/" + taskId
        ));
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "Проверить статус асинхронной задачи по taskId")
    public ResponseEntity<AsyncTaskResponseDto> getTaskStatus(@PathVariable String taskId) {
        return ResponseEntity.ok(asyncBookingService.getTaskStatus(taskId));
    }

    @GetMapping("/race-condition")
    @Operation(summary = "Демонстрация Race Condition (50+ потоков) и решения через AtomicInteger")
    public ResponseEntity<RaceConditionResultDto> testRaceCondition(
            @RequestParam(defaultValue = "60") int threads,
            @RequestParam(defaultValue = "1000") int increments) {
        return ResponseEntity.ok(raceConditionDemoService.runRaceConditionDemo(threads, increments));
    }
}
