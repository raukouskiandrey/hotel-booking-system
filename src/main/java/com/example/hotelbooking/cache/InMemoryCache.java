package com.example.hotelbooking.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Лабораторная 3: In-Memory кэш на основе хэш-таблицы.
 * Обеспечивает кэширование результатов сложных запросов и инвалидацию при изменениях.
 */
@Component
public class InMemoryCache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(InMemoryCache.class);
    private final Map<K, V> cache = new ConcurrentHashMap<>();

    public Optional<V> get(K key) {
        V value = cache.get(key);
        if (value != null) {
            log.info("CACHE HIT для ключа: {}", key);
            return Optional.of(value);
        }
        log.info("CACHE MISS для ключа: {}", key);
        return Optional.empty();
    }

    public void put(K key, V value) {
        log.info("CACHE PUT: сохранение значения по ключу: {}", key);
        cache.put(key, value);
    }

    public void evict(K key) {
        log.info("CACHE EVICT: удаление ключа: {}", key);
        cache.remove(key);
    }

    public void evictAll() {
        log.info("CACHE EVICT ALL: полная инвалидация кэша (было записей: {})", cache.size());
        cache.clear();
    }

    public int size() {
        return cache.size();
    }
}
