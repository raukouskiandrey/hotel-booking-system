package com.example.hotelbooking.service;

import com.example.hotelbooking.cache.InMemoryCache;
import com.example.hotelbooking.dto.request.RoomRequestDto;
import com.example.hotelbooking.dto.response.RoomResponseDto;
import com.example.hotelbooking.exception.BadRequestException;
import com.example.hotelbooking.exception.ResourceNotFoundException;
import com.example.hotelbooking.mapper.RoomMapper;
import com.example.hotelbooking.model.Hotel;
import com.example.hotelbooking.model.Room;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;
    private final InMemoryCache<?, ?> cache;

    @Transactional(readOnly = true)
    public List<RoomResponseDto> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomResponseDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Номер с ID " + id + " не найден"));
        return roomMapper.toDto(room);
    }

    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto dto) {
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + dto.getHotelId() + " не найден"));

        if (roomRepository.existsByHotelIdAndRoomNumber(dto.getHotelId(), dto.getRoomNumber())) {
            throw new BadRequestException("В отеле уже есть номер с номером '" + dto.getRoomNumber() + "'");
        }

        Room room = roomMapper.toEntity(dto, hotel);
        Room saved = roomRepository.save(room);
        cache.evictAll();
        return roomMapper.toDto(saved);
    }

    /**
     * Лабораторная 5: Bulk-операция массового добавления номеров с использованием Stream API и Optional.
     */
    @Transactional
    public List<RoomResponseDto> createRoomsBulk(List<RoomRequestDto> dtos) {
        log.info("Массовое добавление {} номеров через Stream API", dtos.size());

        List<Room> roomsToSave = dtos.stream()
                .map(dto -> {
                    Hotel hotel = hotelRepository.findById(dto.getHotelId())
                            .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + dto.getHotelId() + " не найден"));
                    return roomMapper.toEntity(dto, hotel);
                })
                .collect(Collectors.toList());

        List<Room> saved = roomRepository.saveAll(roomsToSave);
        cache.evictAll();

        return saved.stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Лабораторная 2 & 5: Демонстрация БЕЗ @Transactional (частичное сохранение).
     * Первые записи сохраняются в БД, затем на ошибке выполнение прерывается, но данные остаются в базе.
     */
    @Transactional(propagation = Propagation.NEVER)
    public List<RoomResponseDto> bulkSaveWithoutTransactionalDemo(List<RoomRequestDto> dtos) {
        log.warn("=== ДЕМОНСТРАЦИЯ БЕЗ @Transactional (частичное сохранение в БД) ===");
        List<RoomResponseDto> results = new ArrayList<>();

        for (int i = 0; i < dtos.size(); i++) {
            if (i == 2) {
                throw new RuntimeException("Искусственная ошибка на 3-м элементе! Транзакции нет -> первые 2 номера остались в БД!");
            }
            RoomRequestDto dto = dtos.get(i);
            Hotel hotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + dto.getHotelId() + " не найден"));
            Room room = roomRepository.save(roomMapper.toEntity(dto, hotel));
            results.add(roomMapper.toDto(room));
        }
        return results;
    }

    /**
     * Лабораторная 2 & 5: Демонстрация С @Transactional (полный откат).
     * При возникновении исключения откатываются ВСЕ изменения.
     */
    @Transactional(rollbackFor = Exception.class)
    public List<RoomResponseDto> bulkSaveWithTransactionalDemo(List<RoomRequestDto> dtos) {
        log.info("=== ДЕМОНСТРАЦИЯ С @Transactional (полный откат при ошибке) ===");
        List<RoomResponseDto> results = new ArrayList<>();

        for (int i = 0; i < dtos.size(); i++) {
            if (i == 2) {
                throw new RuntimeException("Искусственная ошибка на 3-м элементе! @Transactional откатит ВСЕ добавленные номера!");
            }
            RoomRequestDto dto = dtos.get(i);
            Hotel hotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + dto.getHotelId() + " не найден"));
            Room room = roomRepository.save(roomMapper.toEntity(dto, hotel));
            results.add(roomMapper.toDto(room));
        }
        return results;
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Номер с ID " + id + " не найден");
        }
        roomRepository.deleteById(id);
        cache.evictAll();
    }
}
