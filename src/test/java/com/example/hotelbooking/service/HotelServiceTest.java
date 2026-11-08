package com.example.hotelbooking.service;

import com.example.hotelbooking.cache.InMemoryCache;
import com.example.hotelbooking.dto.request.HotelRequestDto;
import com.example.hotelbooking.dto.response.HotelResponseDto;
import com.example.hotelbooking.exception.ResourceNotFoundException;
import com.example.hotelbooking.mapper.HotelMapper;
import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.repository.AmenityRepository;
import com.example.hotelbooking.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Лабораторная 5: Модульные тесты сервисного слоя с использованием Mockito.
 */
@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private InMemoryCache<?, ?> cache;

    @InjectMocks
    private HotelService hotelService;

    private Hotel sampleHotel;
    private HotelResponseDto sampleResponseDto;
    private HotelRequestDto sampleRequestDto;

    @BeforeEach
    void setUp() {
        sampleHotel = Hotel.builder()
                .id(1L)
                .name("Отель Минск")
                .city("Минск")
                .address("пр. Независимости 11")
                .rating(4.5)
                .build();

        sampleResponseDto = HotelResponseDto.builder()
                .id(1L)
                .name("Отель Минск")
                .city("Минск")
                .address("пр. Независимости 11")
                .rating(4.5)
                .build();

        sampleRequestDto = HotelRequestDto.builder()
                .name("Отель Минск")
                .city("Минск")
                .address("пр. Независимости 11")
                .rating(4.5)
                .build();
    }

    @Test
    @DisplayName("Успешное получение отеля по ID")
    void getHotelById_Success() {
        when(hotelRepository.findWithDetailsById(1L)).thenReturn(Optional.of(sampleHotel));
        when(hotelMapper.toDto(sampleHotel)).thenReturn(sampleResponseDto);

        HotelResponseDto result = hotelService.getHotelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Отель Минск");
        verify(hotelRepository, times(1)).findWithDetailsById(1L);
    }

    @Test
    @DisplayName("Исключение ResourceNotFoundException, если отель не найден")
    void getHotelById_NotFound_ThrowsException() {
        when(hotelRepository.findWithDetailsById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Отель с ID 999 не найден");

        verify(hotelRepository, times(1)).findWithDetailsById(999L);
    }

    @Test
    @DisplayName("Успешное создание отеля с инвалидацией кэша")
    void createHotel_Success() {
        when(hotelMapper.toEntity(sampleRequestDto)).thenReturn(sampleHotel);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(sampleHotel);
        when(hotelMapper.toDto(sampleHotel)).thenReturn(sampleResponseDto);

        HotelResponseDto result = hotelService.createHotel(sampleRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Отель Минск");
        verify(hotelRepository, times(1)).save(sampleHotel);
        verify(cache, times(1)).evictAll();
    }
}
