package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.request.BookingRequestDto;
import com.example.hotelbooking.dto.response.BookingResponseDto;
import com.example.hotelbooking.exception.BadRequestException;
import com.example.hotelbooking.exception.ResourceNotFoundException;
import com.example.hotelbooking.mapper.BookingMapper;
import com.example.hotelbooking.model.Booking;
import com.example.hotelbooking.model.Guest;
import com.example.hotelbooking.model.Room;
import com.example.hotelbooking.model.enums.BookingStatus;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.GuestRepository;
import com.example.hotelbooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingMapper bookingMapper;

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование с ID " + id + " не найдено"));
        return bookingMapper.toDto(booking);
    }

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        if (dto.getCheckOutDate().isBefore(dto.getCheckInDate()) || dto.getCheckOutDate().isEqual(dto.getCheckInDate())) {
            throw new BadRequestException("Дата выезда должна быть строго позже даты заезда");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Номер с ID " + dto.getRoomId() + " не найден"));

        Guest guest = guestRepository.findById(dto.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Гость с ID " + dto.getGuestId() + " не найден"));

        Booking booking = bookingMapper.toEntity(dto, room, guest);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDto updateStatus(Long id, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование с ID " + id + " не найдено"));

        booking.setStatus(newStatus);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Бронирование с ID " + id + " не найдено");
        }
        bookingRepository.deleteById(id);
    }
}
