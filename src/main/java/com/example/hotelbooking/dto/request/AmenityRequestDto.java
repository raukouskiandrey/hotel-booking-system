package com.example.hotelbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenityRequestDto {

    @NotBlank(message = "Название услуги/удобства обязательно")
    private String name;

    private String description;
}
