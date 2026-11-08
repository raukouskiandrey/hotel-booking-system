package com.example.hotelbooking.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Лабораторная 4: Аспект для замера и логирования времени выполнения сервисных методов.
 */
@Aspect
@Component
public class ExecutionTimeLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeLoggingAspect.class);

    @Around("execution(* com.example.hotelbooking.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("[AOP TIMING] Метод {}.{} выполнен за {} мс", className, methodName, executionTime);
        }
    }
}
