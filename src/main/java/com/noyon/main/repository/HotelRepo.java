package com.noyon.main.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.noyon.main.entities.Hotel;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Integer>{

	

	@Query("select hotel from Hotel hotel where hotel.location.name= :locationName")
	List<Hotel> findHotelByLocationName(@Param("locationName") String locationName);

}