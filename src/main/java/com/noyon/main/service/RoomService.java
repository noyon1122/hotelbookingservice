package com.noyon.main.service;

import java.io.IOException; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.noyon.main.entities.Hotel;
import com.noyon.main.entities.Location;
import com.noyon.main.entities.Room;
import com.noyon.main.repository.HotelRepo;
import com.noyon.main.repository.RoomRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RoomService {

	@Autowired
	private RoomRepo roomRepo;
	
	@Autowired 
	private HotelRepo  hotelRepo;
	
	@Value("src/main/resources/static/images")
	private String uploadDir;
	
	//Save Location details in the database
		public void saveRoom(Room room,MultipartFile imagefile) throws IOException
		{
			if(imagefile !=null && !imagefile.isEmpty())
			{
				String imageFileName=saveImage(room,imagefile);
				room.setImage(imageFileName);
			}
			
			roomRepo.save(room);
		}
		
	    //find all location
		public List<Room> getAllRoom() {
			// TODO Auto-generated method stub
			return roomRepo.findAll();
		}


		//get location by id
		public Room getRoomById(int id) {
			// TODO Auto-generated method stub
			
			return roomRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Room is not not found by this id : "+id));

			
		}

	    //get location by Name
		public Room getRoomByName(String name) {
			// TODO Auto-generated method stub
			
			Room room=roomRepo.findByName(name);
			return room;
		}
		
		//get Room by hotelName
		
		public List<Room> getRoomByHotelName(String hotelName)
		{
			return roomRepo.findRoomByHotelName(hotelName);
		}
		
		//get Room by HotelId
		public List<Room> getRoomByHotelId(int hotelId) {
			return roomRepo.findRoomByHotelId(hotelId);
		}


		//update location by id
		public Room updateRoom(Room updateRoom, int id,MultipartFile image) throws IOException {
			Room existingRoom = roomRepo.findById(id).orElseThrow(
					()-> new EntityNotFoundException("Room is not not found by this id : "+id));
			
			existingRoom.setName(updateRoom.getName());
			existingRoom.setPrice(updateRoom.getPrice());
			existingRoom.setAdultNo(updateRoom.getAdultNo());
			existingRoom.setChildNo(updateRoom.getChildNo());
			
			  Hotel hotel=hotelRepo.findById(updateRoom.getHotel().getId()).orElseThrow(
						()-> new EntityNotFoundException("Location is not not found by this id : "+updateRoom.getHotel().getId())
						);	
			
			 
			  existingRoom.setHotel(hotel);
			  
			  if(image !=null && !image.isEmpty())
			  {
				  String fileName=saveImage(existingRoom, image);
				  existingRoom.setImage(fileName);
			  }
			  roomRepo.save(existingRoom);
			  
			return existingRoom;
		}

	    
		//update delete
		
		public void deleteRoom(int id) {
			// TODO Auto-generated method stub
			roomRepo.deleteById(id);
			
		}

		
		//mathod for image save
		private String saveImage(Room room,MultipartFile file) throws IOException
		{
			Path uploadPath=Paths.get(uploadDir+"/rooms");
			
			if(!Files.exists(uploadPath))
			{
				Files.createDirectories(uploadPath);
			}
			
			String fileName=room.getName()+"_"+UUID.randomUUID().toString();
			
			Path filePath=uploadPath.resolve(fileName);
			
			Files.copy(file.getInputStream(),filePath);
			
			return fileName;
		}
}
