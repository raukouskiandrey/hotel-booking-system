package com.example.hotelbooking.model;

import com.example.hotelbooking.model.enums.AsyncTaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsyncTask {
    private String taskId;
    private AsyncTaskStatus status;
    private String result;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
