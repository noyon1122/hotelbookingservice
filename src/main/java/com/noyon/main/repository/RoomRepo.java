package com.noyon.main.repository;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.noyon.main.entities.Room;

public interface RoomRepo extends JpaRepository<Room, Integer>{

	

	

	Room findByName(String name);
    @Query("select room from Room room where room.hotel.name= :hotelName")
  
	List<Room> findRoomByHotelName(String hotelName);
    @Query("select room from Room room where room.hotel.id= :hotelId")
	List<Room> findRoomByHotelId(int hotelId);

}
