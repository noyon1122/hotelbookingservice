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
import com.noyon.main.repository.HotelRepo;
import com.noyon.main.repository.LocationRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HotelService {

	@Autowired 
	private HotelRepo hotelRepo;
	
	@Autowired
	private LocationRepo locationRepo;
	
	
	@Value("src/main/resources/static/images")
	private String uploadDir;
	
	//Save hotel details in the database
	public void saveHotel(Hotel hotel,MultipartFile imagefile) throws IOException
	{
		if(imagefile !=null && !imagefile.isEmpty())
		{
			String imageFileName=saveImage(hotel, imagefile);
			hotel.setImage(imageFileName);
		}
		
		//set up hotel information
		Location location=locationRepo.findById(hotel.getLocation().getId()).orElseThrow(
				()-> new EntityNotFoundException("Hotel is not not found by this id : "+hotel.getLocation().getId())
				);
		hotel.setLocation(location);
		hotelRepo.save(hotel);
	}
	
    //find all hotel
	public List<Hotel> getAllHotel() {
		// TODO Auto-generated method stub
		return hotelRepo.findAll();
	}


	//get hotel by id
	public Hotel getHotelById(int id) {
		// TODO Auto-generated method stub
		
		return hotelRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Hotel is not not found by this id : "+id));

		
	}
  
	public List<Hotel> getHotelByLocationName(String locationName) {
		return hotelRepo.findHotelByLocationName(locationName);
	}
	
  //update hotel
	public Hotel updateHotel(Hotel updateHotel, int id, MultipartFile image) throws IOException {
		Hotel existingHotel = hotelRepo.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Hotel is not not found by this id : "+id));
		
		
			  existingHotel.setName(updateHotel.getName());
			  existingHotel.setAddress(updateHotel.getAddress());
			  existingHotel.setMaxPrice(updateHotel.getMaxPrice());
			  existingHotel.setMinPrice(updateHotel.getMinPrice());
			  existingHotel.setRating(updateHotel.getRating());
			  
		      //update location
			  Location location=locationRepo.findById(updateHotel.getLocation().getId()).orElseThrow(
						()-> new EntityNotFoundException("Location is not not found by this id : "+updateHotel.getLocation().getId())
						);		  
			  
			  
		  existingHotel.setLocation(location);
		  
		  //update image
		  
		  if(image !=null && !image.isEmpty())
		  {
			  String fileName=saveImage(existingHotel, image);
			  existingHotel.setImage(fileName);
		  }
		 
		  
		  hotelRepo.save(existingHotel);
		  return existingHotel;
		  
		
	}

    
	//delete hotel 
	
	public void deleteHotel(int id) {
		// TODO Auto-generated method stub
		hotelRepo.deleteById(id);
		
	}

	
	//mathod for image save
	private String saveImage(Hotel hotel,MultipartFile file) throws IOException
	{
		Path uploadPath=Paths.get(uploadDir+"/hotels");
		
		if(!Files.exists(uploadPath))
		{
			Files.createDirectories(uploadPath);
		}
		
		String fileName=hotel.getName()+"_"+UUID.randomUUID().toString();
		
		Path filePath=uploadPath.resolve(fileName);
		
		Files.copy(file.getInputStream(),filePath);
		
		return fileName;
	}
}
