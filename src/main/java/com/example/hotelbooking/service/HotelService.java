package com.example.hotelbooking.service;

import com.example.hotelbooking.cache.CacheKey;
import com.example.hotelbooking.cache.InMemoryCache;
import com.example.hotelbooking.dto.request.HotelRequestDto;
import com.example.hotelbooking.dto.response.HotelResponseDto;
import com.example.hotelbooking.exception.ResourceNotFoundException;
import com.example.hotelbooking.mapper.HotelMapper;
import com.example.hotelbooking.model.Amenity;
import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.repository.AmenityRepository;
import com.example.hotelbooking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;
    private final InMemoryCache<CacheKey, List<HotelResponseDto>> cache;

    @Transactional(readOnly = true)
    public Page<HotelResponseDto> getHotelsByCityAndRating(String city, Double minRating, Pageable pageable) {
        return hotelRepository.findByCityIgnoreCaseAndRatingGreaterThanEqual(city, minRating, pageable)
                .map(hotelMapper::toDto);
    }

    @Transactional(readOnly = true)
    public HotelResponseDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + id + " не найден"));
        return hotelMapper.toDto(hotel);
    }

    /**
     * Лабораторная 2: Демонстрация проблемы N+1.
     * При обращении к ленивым связям rooms в цикле Hibernate генерирует N дополнительных SQL-запросов.
     */
    @Transactional(readOnly = true)
    public List<String> demonstrateNPlusOneProblem() {
        log.warn("=== ДЕМОНСТРАЦИЯ ПРОБЛЕМЫ N+1 (смотрим в логи SQL) ===");
        List<Hotel> hotels = hotelRepository.findAllWithoutEntityGraph();
        return hotels.stream()
                .map(h -> "Отель: " + h.getName() + ", Комнат: " + h.getRooms().size()) // Trigger N SELECTs
                .collect(Collectors.toList());
    }

    /**
     * Лабораторная 2: Решение проблемы N+1 с помощью @EntityGraph.
     * Все связанные комнаты подтягиваются одним JOIN-запросом.
     */
    @Transactional(readOnly = true)
    public List<String> demonstrateNPlusOneSolution() {
        log.info("=== РЕШЕНИЕ ПРОБЛЕМЫ N+1 ЧЕРЕЗ @EntityGraph ===");
        List<Hotel> hotels = hotelRepository.findAllWithRoomsEntityGraph();
        return hotels.stream()
                .map(h -> "Отель: " + h.getName() + ", Комнат: " + h.getRooms().size())
                .collect(Collectors.toList());
    }

    /**
     * Лабораторная 3: Сложный JPQL-запрос с фильтрацией по номерам и кэшированием в InMemoryCache.
     */
    @Transactional(readOnly = true)
    public List<HotelResponseDto> searchHotelsWithCache(String city, Double maxPrice, int page, int size) {
        CacheKey cacheKey = new CacheKey(city, maxPrice, page, size);

        Optional<List<HotelResponseDto>> cached = cache.get(cacheKey);
        if (cached.isPresent()) {
            return cached.get();
        }

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Hotel> resultPage = hotelRepository.findHotelsByCityAndMaxRoomPrice(city, maxPrice, pageable);

        List<HotelResponseDto> dtoList = resultPage.getContent().stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());

        cache.put(cacheKey, dtoList);
        return dtoList;
    }

    /**
     * Лабораторная 3: Аналогичный запрос через Native Query.
     */
    @Transactional(readOnly = true)
    public List<HotelResponseDto> searchHotelsNative(String city, Double maxPrice, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Hotel> resultPage = hotelRepository.findHotelsByCityAndMaxRoomPriceNative(city, maxPrice, pageable);
        return resultPage.getContent().stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public HotelResponseDto createHotel(HotelRequestDto dto) {
        Hotel hotel = hotelMapper.toEntity(dto);

        if (dto.getAmenityIds() != null && !dto.getAmenityIds().isEmpty()) {
            Set<Amenity> amenities = new HashSet<>(amenityRepository.findAllById(dto.getAmenityIds()));
            hotel.setAmenities(amenities);
        }

        Hotel saved = hotelRepository.save(hotel);
        cache.evictAll(); // Инвалидация кэша при создании нового отеля
        return hotelMapper.toDto(saved);
    }

    @Transactional
    public HotelResponseDto updateHotel(Long id, HotelRequestDto dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + id + " не найден"));

        hotel.setName(dto.getName());
        hotel.setCity(dto.getCity());
        hotel.setAddress(dto.getAddress());
        hotel.setRating(dto.getRating());

        if (dto.getAmenityIds() != null) {
            Set<Amenity> amenities = new HashSet<>(amenityRepository.findAllById(dto.getAmenityIds()));
            hotel.setAmenities(amenities);
        }

        Hotel updated = hotelRepository.save(hotel);
        cache.evictAll(); // Инвалидация кэша при обновлении
        return hotelMapper.toDto(updated);
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Отель с ID " + id + " не найден");
        }
        hotelRepository.deleteById(id);
        cache.evictAll(); // Инвалидация кэша при удалении
    }
}
