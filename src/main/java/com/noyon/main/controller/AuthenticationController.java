package com.noyon.main.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.noyon.main.entities.AuthenticationResponse;
import com.noyon.main.entities.User;
import com.noyon.main.service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestPart("user") User user,@RequestParam("image")MultipartFile file) throws IOException
	{
		
		return ResponseEntity.ok(authService.register(user, file));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody User user)
	{
		return ResponseEntity.ok(authService.login(user));
	}
	
	@GetMapping("/active/{id}")
	
	public ResponseEntity<String> activeUser(@PathVariable long id)
	{
		String response=authService.activeUser(id);
		return ResponseEntity.ok(response);
	}
}
