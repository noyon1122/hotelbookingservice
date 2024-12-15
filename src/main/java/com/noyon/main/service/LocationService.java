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

import com.noyon.main.entities.Location;
import com.noyon.main.repository.LocationRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LocationService {

	@Autowired 
	private LocationRepo locationRepo;
	
	
	@Value("src/main/resources/static/images")
	private String uploadDir;
	
	//Save Location details in the database
	public void saveLocation(Location location,MultipartFile imagefile) throws IOException
	{
		if(imagefile !=null && !imagefile.isEmpty())
		{
			String imageFileName=saveImage(location,imagefile);
			location.setImage(imageFileName);
		}
		
		locationRepo.save(location);
	}
	
    //find all location
	public List<Location> getAllLocation() {
		// TODO Auto-generated method stub
		return locationRepo.findAll();
	}


	//get location by id
	public Location getLocationById(int id) {
		// TODO Auto-generated method stub
		
		return locationRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Location is not not found by this id : "+id));
//		Optional<Location> location=locationRepo.findById(id);
//				
//		if(location.isPresent())
//		{
//			return location.get();
//		}
//		else {
//			return null;
//		}
		
	}

    //get location by Name
	public Location getLocationByName(String name) {
		// TODO Auto-generated method stub
		
		Location location=locationRepo.findByName(name);
		return location;
	}


	//update location by id
	public Location updateLocation(Location updatelocation, int id,MultipartFile image) throws IOException {
		Location existingLocation = locationRepo.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Location is not not found by this id : "+id));
		
		  if(updatelocation.getName()!=null)
		  {
			  existingLocation.setName(updatelocation.getName());
		  }
		  
		  if(image !=null && !image.isEmpty())
		  {
			  String fileName=saveImage(existingLocation, image);
			  existingLocation.setImage(fileName);
		  }
		 
		  
		  locationRepo.save(existingLocation);
		  
		return existingLocation;
	}

    
	//update delete
	
	public void deleteLocation(int id) {
		// TODO Auto-generated method stub
		locationRepo.deleteById(id);
		
	}

	
	//mathod for image save
	private String saveImage(Location location,MultipartFile file) throws IOException
	{
		Path uploadPath=Paths.get(uploadDir+"/locations");
		
		if(!Files.exists(uploadPath))
		{
			Files.createDirectories(uploadPath);
		}
		
		String fileName=location.getName()+"_"+UUID.randomUUID().toString();
		
		Path filePath=uploadPath.resolve(fileName);
		
		Files.copy(file.getInputStream(),filePath);
		
		return fileName;
	}
}
