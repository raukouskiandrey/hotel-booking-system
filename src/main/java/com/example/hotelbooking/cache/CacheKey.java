package com.example.hotelbooking.cache;

import lombok.Getter;

import java.util.Objects;

/**
 * Лабораторная 3: Составной ключ кэша на основе параметров запроса.
 * Корректная работа HashMap гарантируется реализацией equals() и hashCode().
 */
@Getter
public class CacheKey {
    private final String city;
    private final Double maxPrice;
    private final int page;
    private final int size;

    public CacheKey(String city, Double maxPrice, int page, int size) {
        this.city = city != null ? city.trim().toLowerCase() : "";
        this.maxPrice = maxPrice;
        this.page = page;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return page == cacheKey.page &&
                size == cacheKey.size &&
                Objects.equals(city, cacheKey.city) &&
                Objects.equals(maxPrice, cacheKey.maxPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, maxPrice, page, size);
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "city='" + city + '\'' +
                ", maxPrice=" + maxPrice +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
