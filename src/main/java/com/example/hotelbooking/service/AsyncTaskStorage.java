package com.example.hotelbooking.service;

import com.example.hotelbooking.model.AsyncTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Лабораторная 6: Хранилище асинхронных задач.
 */
@Component
public class AsyncTaskStorage {

    private final Map<String, AsyncTask> tasks = new ConcurrentHashMap<>();

    public void save(AsyncTask task) {
        tasks.put(task.getTaskId(), task);
    }

    public Optional<AsyncTask> findById(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }
}
