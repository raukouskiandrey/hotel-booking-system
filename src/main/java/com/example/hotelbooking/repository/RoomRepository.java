package com.example.hotelbooking.repository;

import com.example.hotelbooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    boolean existsByHotelIdAndRoomNumber(Long hotelId, String roomNumber);
}
