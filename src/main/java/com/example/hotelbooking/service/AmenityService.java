package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.request.AmenityRequestDto;
import com.example.hotelbooking.dto.response.AmenityResponseDto;
import com.example.hotelbooking.exception.BadRequestException;
import com.example.hotelbooking.exception.ResourceNotFoundException;
import com.example.hotelbooking.mapper.AmenityMapper;
import com.example.hotelbooking.model.Amenity;
import com.example.hotelbooking.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityService {

    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    @Transactional(readOnly = true)
    public List<AmenityResponseDto> getAllAmenities() {
        return amenityRepository.findAll().stream()
                .map(amenityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AmenityResponseDto getAmenityById(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Удобство с ID " + id + " не найдено"));
        return amenityMapper.toDto(amenity);
    }

    @Transactional
    public AmenityResponseDto createAmenity(AmenityRequestDto dto) {
        if (amenityRepository.findByName(dto.getName()).isPresent()) {
            throw new BadRequestException("Удобство с названием '" + dto.getName() + "' уже существует");
        }
        Amenity amenity = amenityMapper.toEntity(dto);
        return amenityMapper.toDto(amenityRepository.save(amenity));
    }

    @Transactional
    public AmenityResponseDto updateAmenity(Long id, AmenityRequestDto dto) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Удобство с ID " + id + " не найдено"));
        amenity.setName(dto.getName());
        amenity.setDescription(dto.getDescription());
        return amenityMapper.toDto(amenityRepository.save(amenity));
    }

    @Transactional
    public void deleteAmenity(Long id) {
        if (!amenityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Удобство с ID " + id + " не найдено");
        }
        amenityRepository.deleteById(id);
    }
}
