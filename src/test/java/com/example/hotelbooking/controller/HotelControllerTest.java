package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.request.HotelRequestDto;
import com.example.hotelbooking.dto.response.HotelResponseDto;
import com.example.hotelbooking.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Лабораторная 5: Тестирование контроллеров с MockMvc (standalone setup).
 */
@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
    }

    @Test
    @DisplayName("GET /api/v1/hotels/{id} возвращает 200 OK")
    void getHotelById_ReturnsOk() throws Exception {
        HotelResponseDto responseDto = HotelResponseDto.builder()
                .id(1L)
                .name("Отель Европа")
                .city("Минск")
                .address("ул. Ленина 28")
                .rating(4.8)
                .build();

        when(hotelService.getHotelById(eq(1L))).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Отель Европа"))
                .andExpect(jsonPath("$.city").value("Минск"));
    }
}
