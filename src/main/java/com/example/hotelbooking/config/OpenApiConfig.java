package com.example.hotelbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Booking System API")
                        .description("REST API для системы бронирования отелей. Выполнение требований лабораторных работ 1-8 (БГУИР, ФКСИС).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Равковский Андрей")
                                .email("raukouskiandrey@gmail.com")));
    }
}
