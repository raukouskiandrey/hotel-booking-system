package com.example.hotelbooking.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceConditionResultDto {
    private int totalThreads;
    private int incrementsPerThread;
    private int expectedCount;
    private int unsafeResult;
    private int safeResult;
    private String explanation;
}
