package com.noyon.main.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.noyon.main.entities.Location;

@Repository
public interface LocationRepo extends JpaRepository<Location, Integer> {

	Location findByName(String name);

}