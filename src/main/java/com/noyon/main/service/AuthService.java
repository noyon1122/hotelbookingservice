package com.noyon.main.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.noyon.main.entities.AuthenticationResponse;
import com.noyon.main.entities.Hotel;
import com.noyon.main.entities.Role;
import com.noyon.main.entities.Token;
import com.noyon.main.entities.User;
import com.noyon.main.jwt.JwtService;
import com.noyon.main.repository.TokenRepo;
import com.noyon.main.repository.UserRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;

@Service
public class AuthService {

	private final UserRepo userRepo;
	private final TokenRepo tokenRepo;
	private final JwtService jwtService;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	@Value("src/main/resources/static/images")
	private String uploadDir;
	public AuthService(UserRepo userRepo, TokenRepo tokenRepo, JwtService jwtService, EmailService emailService,
			PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
		super();
		this.userRepo = userRepo;
		this.tokenRepo = tokenRepo;
		this.jwtService = jwtService;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}
	
	
	private void saveUserToken(String jwt,User user)
	{
		Token token= new Token();
		token.setToken(jwt);
		token.setLogout(false);
		token.setUser(user);
		tokenRepo.save(token);
	}
	
	private void removeAllTokenByUser(User user)
	{
		List<Token> validTokens=tokenRepo.findAllTokenByUserId(user.getId());
		if(validTokens.isEmpty())
		{
			return;
		}
		
		else {
			  validTokens.forEach(t -> {
				  t.setLogout(true);
			  });
		}
		tokenRepo.saveAll(validTokens);
		
		
	}
	
	
	//User Registration
	
	public AuthenticationResponse register(User user,MultipartFile imageFile) throws IOException
	{
		if(userRepo.findByEmail(user.getEmail()).isPresent())
		{
			return new AuthenticationResponse(null,"User Already exists for this email");
		}
		
		if(imageFile !=null && ! imageFile.isEmpty())
		{
			String imageFileName=saveImage(user, imageFile);
			user.setImage(imageFileName);
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.valueOf("USER"));
		user.setActive(false);
		user.setLock(true);
		
		userRepo.save(user);
		
		String jwt=jwtService.generatedToken(user);
		saveUserToken(jwt, user);
		sendActivation(user);
		
		return new AuthenticationResponse(jwt,"User register successfully!!");
		
	}
	
	
	
	
	private void sendActivation(User user) {
	
		String activationLink="http://localhost:8080/active/"+user.getId();
		
		String mailText="<h2> Dear </h2>"+user.getName()+","
				+"<p> Please Click on the following Link to confirm your registration"
				+"<a href=\""+activationLink+"\">Active Account</a>";
		String subject="Confirmation Registration";
		
		try {
			emailService.sendSimpleEmail(user.getEmail(), subject, mailText);
		}
		catch (MessagingException e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
		
	}

	
	public AuthenticationResponse login(User user)
	{
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						user.getUsername(),
						user.getPassword()
						)
				);
		
		String jwt=jwtService.generatedToken(user);
		
		removeAllTokenByUser(user);
		saveUserToken(jwt, user);
		
		return new AuthenticationResponse(jwt,"User Login Succesfully");
	}
	
	
	public String activeUser(long id)
	{
		User user=userRepo.findById(id).orElseThrow(
				()-> new RuntimeException("User not found with this id"+id));
		
		if(user !=null)
		{
			user.setActive(true);
			userRepo.save(user);
			return "User Activation Successfully!";
		}
		else {
			return "Invalid activation Token!";
		}
	}
	

	//mathod for image save
     private   String saveImage(User user,MultipartFile imageFile) throws IOException
	{
		Path uploadPath=Paths.get(uploadDir+"/users");
		
		if(!Files.exists(uploadPath))
		{
			Files.createDirectories(uploadPath);
		}
		
		String fileName=user.getName()+"_"+UUID.randomUUID().toString();
		
		Path filePath=uploadPath.resolve(fileName);
		
		Files.copy(imageFile.getInputStream(),filePath);
		
		return fileName;
	}
}
