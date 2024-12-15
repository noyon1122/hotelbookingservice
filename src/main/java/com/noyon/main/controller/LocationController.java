package com.noyon.main.controller;

import java.io.IOException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.noyon.main.entities.Location;
import com.noyon.main.service.LocationService;

@RestController
@RequestMapping("/api/location")
public class LocationController {

	
	@Autowired
	private LocationService locationService;
	
	
	//save location with image
	@PostMapping("/save")
	public ResponseEntity<String> saveLocaton(@RequestPart Location location , @RequestParam(value = "image", required = true) MultipartFile file) throws IOException
	{
		locationService.saveLocation(location, file);
		return new ResponseEntity<String>("Location is saved successfully!!",HttpStatus.CREATED);
	}
	
	//get all location
	@GetMapping("/")
	public ResponseEntity<List<Location>> getAllLocation()
	{
		List<Location> locationList=locationService.getAllLocation();
		return ResponseEntity.ok(locationList);
	}
	
	//get location by id
	
	@GetMapping("/{id}")
	public ResponseEntity<Location> getLocationById(@PathVariable int id)
	{
		Location location =locationService.getLocationById(id);
		
		return ResponseEntity.ok(location);
	}
	
	
	//get location by name
	
		@GetMapping("name/{name}")
		public ResponseEntity<Location> getLocationByName(@PathVariable String name)
		{
			Location location =locationService.getLocationByName(name);
			
			return ResponseEntity.ok(location);
		}
	
	
	//delete location by id
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteLocation(@PathVariable int id)
	{
		try {
			locationService.deleteLocation(id);
			return ResponseEntity.ok("Location is deleted successfully by this id : !!"+id);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	//update location by id
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Location> updateLocationById(
			@PathVariable int id, 
			@RequestPart Location location,
			@RequestParam(value = "image") MultipartFile file) throws IOException
	{
		Location updateLocation=locationService.updateLocation(location, id,file);
		return ResponseEntity.ok(updateLocation);
	}
}