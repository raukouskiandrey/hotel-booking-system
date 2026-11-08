package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.response.RaceConditionResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Лабораторная 6: Демонстрация Race Condition (состояния гонки) при 50+ потоках
 * и его решение с использованием потокобезопасного счетчика AtomicInteger / synchronized.
 */
@Service
public class RaceConditionDemoService {

    private static final Logger log = LoggerFactory.getLogger(RaceConditionDemoService.class);

    private int unsafeCounter = 0;
    private final AtomicInteger safeCounter = new AtomicInteger(0);

    public RaceConditionResultDto runRaceConditionDemo(int threads, int incrementsPerThread) {
        unsafeCounter = 0;
        safeCounter.set(0);

        int expectedCount = threads * incrementsPerThread;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        // Неатомарная операция: чтение -> изменение -> запись (Race Condition!)
                        unsafeCounter++;

                        // Потокобезопасная атомарная операция на основе аппаратного CAS (Compare-And-Swap)
                        safeCounter.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        log.warn("Ожидалось: {}, Небезопасный результат: {}, Атомарный результат: {}",
                expectedCount, unsafeCounter, safeCounter.get());

        return RaceConditionResultDto.builder()
                .totalThreads(threads)
                .incrementsPerThread(incrementsPerThread)
                .expectedCount(expectedCount)
                .unsafeResult(unsafeCounter)
                .safeResult(safeCounter.get())
                .explanation("В небезопасном счетчике из-за гонки потоков (Race Condition) значение меньше ожидаемого (" +
                        unsafeCounter + " < " + expectedCount + "). В safeCounter (AtomicInteger) значение строго равно " +
                        safeCounter.get() + ".")
                .build();
    }
}
