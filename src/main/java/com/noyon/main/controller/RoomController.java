package com.noyon.main.controller;
import java.io.IOException;
import java.util.List;

import org.hibernate.Internal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.noyon.main.entities.Room;
import com.noyon.main.service.RoomService;

@RestController
@RequestMapping("/api/room")
public class RoomController {

	@Autowired
	private RoomService roomService;
	
	//get all rooms
	
	@GetMapping("/")
	public ResponseEntity<List<Room>> getAllRoom()
	{
		List<Room> allRooms= roomService.getAllRoom();
		
		return ResponseEntity.ok(allRooms);
	}
	
	//save room
	@PostMapping("/save")
	public ResponseEntity<String> saveRoom(
			@RequestPart(value = "room") Room room,
			@RequestParam(value = "image") MultipartFile file
			) throws IOException
	{
		roomService.saveRoom(room, file);
		return new ResponseEntity<String>("Room saved successfully!!",HttpStatus.CREATED);
	}
	
	//find by id
	
	@GetMapping("/{id}")
	public ResponseEntity<Room> getRoomById(@PathVariable int id)
	{
		Room room=roomService.getRoomById(id);
		return ResponseEntity.ok(room);
	}
	
	//find by name
	
	@GetMapping("/searchby/{name}")
	public ResponseEntity<Room> getRoomByName(@PathVariable String name)
	{
		Room room=roomService.getRoomByName(name);
		
		return ResponseEntity.ok(room);
	}
	
	//find Room by hotel name
	
	@GetMapping("/r/findRoomByHotelName")
	public ResponseEntity<List<Room>> getRoomByHotelName(@RequestParam(value = "hotelName") String hotelName)
	{
		List<Room> rooms=roomService.getRoomByHotelName(hotelName);
		return ResponseEntity.ok(rooms);
	}
	
	@GetMapping("/r/findRoomByHotelId")
	public ResponseEntity<List<Room>> getRoomByHotelName(@RequestParam(value = "hotelId") int hotelId)
	{
		List<Room> rooms=roomService.getRoomByHotelId(hotelId);
		return ResponseEntity.ok(rooms);
	}
	//update room
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Room> updateRoom(
			@RequestPart(value="room") Room room,
			@PathVariable int id,
			@RequestParam(value="image") MultipartFile file
			) throws IOException
	{
		Room updateroom=roomService.updateRoom(room, id, file);
		return ResponseEntity.ok(updateroom);
	}
	
	//delete room
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteRoom(@PathVariable int id)
	{
		try {
			roomService.deleteRoom(id);
			return ResponseEntity.ok("Room is deleted Successfully!! ");
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
