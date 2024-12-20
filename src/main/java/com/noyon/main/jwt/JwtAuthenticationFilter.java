package com.noyon.main.jwt;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.noyon.main.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter  {

	private final JwtService jwtService;
	private final UserService userService;
	public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
		super();
		this.jwtService = jwtService;
		this.userService = userService;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {

            String authHeader=request.getHeader("Authorization");
            
            if(authHeader == null || !authHeader.startsWith("Bearer "))
            {
            	filterChain.doFilter(request, response);
            }
            
            String token=authHeader.substring(7);
            
            String userName=jwtService.extractUserName(token);
            
            UserDetails userDetails=userService.loadUserByUsername(userName);
            
            if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null)
            {
            	UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
            			userDetails,null, userDetails.getAuthorities()
            			);
            	
            	authenticationToken.setDetails(
            		  new WebAuthenticationDetailsSource().buildDetails(request)
            			);
            	SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            
            filterChain.doFilter(request, response);
		
	}
	
	

}
