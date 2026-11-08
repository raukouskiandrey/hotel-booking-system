package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.response.AsyncTaskResponseDto;
import com.example.hotelbooking.exception.ResourceNotFoundException;
import com.example.hotelbooking.model.AsyncTask;
import com.example.hotelbooking.model.enums.AsyncTaskStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Лабораторная 6: Асинхронная бизнес-операция через @Async / CompletableFuture.
 */
@Service
@RequiredArgsConstructor
public class AsyncBookingService {

    private static final Logger log = LoggerFactory.getLogger(AsyncBookingService.class);
    private final AsyncTaskStorage taskStorage;

    public String startAsyncReportGeneration(String city) {
        String taskId = UUID.randomUUID().toString();

        AsyncTask task = AsyncTask.builder()
                .taskId(taskId)
                .status(AsyncTaskStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .build();

        taskStorage.save(task);

        // Запуск в отдельном потоке пула
        processReportAsync(taskId, city);

        return taskId;
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> processReportAsync(String taskId, String city) {
        log.info("[ASYNC] Начало фоновой генерации отчета для задачи ID: {}, город: {}", taskId, city);
        try {
            // Имитация длительной бизнес-логики (аналитика, сбор данных, формирование выписки)
            Thread.sleep(3000);

            AsyncTask task = taskStorage.findById(taskId)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

            task.setStatus(AsyncTaskStatus.COMPLETED);
            task.setResult("Аналитический отчет по отелям города " + city + " успешно сформирован. Всего обработано отелей: 42.");
            task.setCompletedAt(LocalDateTime.now());
            taskStorage.save(task);

            log.info("[ASYNC] Задача {} успешно завершена!", taskId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AsyncTask task = taskStorage.findById(taskId).orElse(null);
            if (task != null) {
                task.setStatus(AsyncTaskStatus.FAILED);
                task.setErrorMessage("Операция прервана: " + e.getMessage());
                task.setCompletedAt(LocalDateTime.now());
                taskStorage.save(task);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    public AsyncTaskResponseDto getTaskStatus(String taskId) {
        AsyncTask task = taskStorage.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Асинхронная задача с ID " + taskId + " не найдена"));

        return AsyncTaskResponseDto.builder()
                .taskId(task.getTaskId())
                .status(task.getStatus())
                .result(task.getResult())
                .errorMessage(task.getErrorMessage())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }
}
