package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.request.GuestRequestDto;
import com.example.hotelbooking.dto.response.GuestResponseDto;
import com.example.hotelbooking.exception.BadRequestException;
import com.example.hotelbooking.exception.ResourceNotFoundException;
import com.example.hotelbooking.mapper.GuestMapper;
import com.example.hotelbooking.model.Guest;
import com.example.hotelbooking.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Transactional(readOnly = true)
    public List<GuestResponseDto> getAllGuests() {
        return guestRepository.findAll().stream()
                .map(guestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GuestResponseDto getGuestById(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Гость с ID " + id + " не найден"));
        return guestMapper.toDto(guest);
    }

    @Transactional
    public GuestResponseDto createGuest(GuestRequestDto dto) {
        if (guestRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Гость с email '" + dto.getEmail() + "' уже зарегистрирован");
        }
        Guest guest = guestMapper.toEntity(dto);
        return guestMapper.toDto(guestRepository.save(guest));
    }

    @Transactional
    public GuestResponseDto updateGuest(Long id, GuestRequestDto dto) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Гость с ID " + id + " не найден"));

        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setPhoneNumber(dto.getPhoneNumber());

        return guestMapper.toDto(guestRepository.save(guest));
    }

    @Transactional
    public void deleteGuest(Long id) {
        if (!guestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Гость с ID " + id + " не найден");
        }
        guestRepository.deleteById(id);
    }
}
