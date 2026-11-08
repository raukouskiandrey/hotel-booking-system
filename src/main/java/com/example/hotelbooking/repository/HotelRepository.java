package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Лабораторная 1: Поиск по городу и минимальному рейтингу (@RequestParam)
    Page<Hotel> findByCityIgnoreCaseAndRatingGreaterThanEqual(String city, Double rating, Pageable pageable);

    // Лабораторная 2: Решение проблемы N+1 с помощью @EntityGraph
    @EntityGraph(attributePaths = {"rooms", "amenities"})
    @Query("SELECT h FROM Hotel h WHERE h.id = :id")
    Optional<Hotel> findWithDetailsById(@Param("id") Long id);

    // Метод для демонстрации N+1 без @EntityGraph (использует findAll)
    @Query("SELECT h FROM Hotel h")
    List<Hotel> findAllWithoutEntityGraph();

    // Метод с решением N+1 для списка
    @EntityGraph(attributePaths = {"rooms"})
    @Query("SELECT h FROM Hotel h")
    List<Hotel> findAllWithRoomsEntityGraph();

    // Лабораторная 3: Сложный JPQL-запрос с фильтрацией по вложенной сущности Room и пагинацией
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.rooms r " +
           "WHERE LOWER(h.city) = LOWER(:city) AND r.pricePerNight <= :maxPrice")
    Page<Hotel> findHotelsByCityAndMaxRoomPrice(@Param("city") String city,
                                               @Param("maxPrice") Double maxPrice,
                                               Pageable pageable);

    // Лабораторная 3: Аналогичный запрос через Native Query
    @Query(value = "SELECT DISTINCT h.* FROM hotels h " +
                   "JOIN rooms r ON h.id = r.hotel_id " +
                   "WHERE LOWER(h.city) = LOWER(:city) AND r.price_per_night <= :maxPrice",
           countQuery = "SELECT COUNT(DISTINCT h.id) FROM hotels h " +
                        "JOIN rooms r ON h.id = r.hotel_id " +
                        "WHERE LOWER(h.city) = LOWER(:city) AND r.price_per_night <= :maxPrice",
           nativeQuery = true)
    Page<Hotel> findHotelsByCityAndMaxRoomPriceNative(@Param("city") String city,
                                                     @Param("maxPrice") Double maxPrice,
                                                     Pageable pageable);
}
