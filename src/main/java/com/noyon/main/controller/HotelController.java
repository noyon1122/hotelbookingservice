package com.noyon.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noyon.main.entities.Hotel;
import com.noyon.main.service.HotelService;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

	@Autowired
	private HotelService hotelService;
	
	//save location with image
		@PostMapping("/save")
		public ResponseEntity<Map<String, String>> saveHotel(
				@RequestPart(value = "hotel") String hotelJson,
				@RequestParam(value = "image") MultipartFile file) throws JsonMappingException, JsonProcessingException  
		{
			ObjectMapper objectMapper=new ObjectMapper();
			Hotel hotel=objectMapper.readValue(hotelJson, Hotel.class);
			
			try {
				hotelService.saveHotel(hotel, file);
				Map<String, String> response=new HashMap<>();
				response.put("Message","Hotel Added Successfully");
				return new ResponseEntity<>(response,HttpStatus.CREATED);
			}
			catch (Exception e) {
				// TODO: handle exception
				
				Map<String, String> errorResponse=new HashMap<>();
				errorResponse.put("Message","Hotel Add failed");
				return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
		//get all location
		@GetMapping("/")
		public ResponseEntity<List<Hotel>> getAllHotel()
		{
			List<Hotel> hotelList=hotelService.getAllHotel();
			return ResponseEntity.ok(hotelList);
		}
		
		//get location by id
		
		@GetMapping("/{id}")
		public ResponseEntity<Hotel> getHotelById(@PathVariable int id)
		{
			try {
				Hotel hotel =hotelService.getHotelById(id);
				return ResponseEntity.ok(hotel);
				
			} catch (RuntimeException e) {
				// TODO: handle exception
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			
		}
		
		//get hotel by location name
		@GetMapping("/searchhotel")
		public ResponseEntity<List<Hotel>> getHotelByLocationName(@RequestParam(value = "locationName") String locationName)
		{
			List<Hotel> hotelList=hotelService.getHotelByLocationName(locationName);
			return ResponseEntity.ok(hotelList);
		}
		
		
		//delete location by id
		
//		@DeleteMapping("/delete/{id}")
//		public ResponseEntity<String> deleteHotel(@PathVariable int id)
//		{
//			try {
//				hotelService.deleteHotel(id);
//				return ResponseEntity.ok("Hotel is deleted successfully by this id : !!"+id);
//			} catch (Exception e) {
//				// TODO: handle exception
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//			}
//		}
//		
//		//update location by id
//		
//		@PutMapping("/update/{id}")
//		public ResponseEntity<Hotel> updateHotelById(@PathVariable int id, @RequestBody Hotel hotel,@RequestParam(value = "image", required = true) MultipartFile file) throws IOException
//		{
//			Hotel updateHotel=hotelService.updateHotel(hotel, id, file);
//			return ResponseEntity.ok(updateHotel);
//		}
}
