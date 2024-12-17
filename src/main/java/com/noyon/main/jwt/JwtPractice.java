package com.noyon.main.jwt;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.eclipse.angus.mail.handlers.text_html;
import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.noyon.main.entities.User;
import com.noyon.main.repository.TokenRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtPractice {

	@Autowired
	private TokenRepo tokenRepo;
	
	private final String SECURITY_KEY="d169552a202ace4ed9b31a326df08noyon3e197a10213030f7c4be596ba99b6";
	
	
	public Claims extractAllClaims(String token)
	{
		return Jwts
				.parser()
				.setSigningKey(getSigninKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public SecretKey getSigninKey()
	{
		byte [] keyBytes=Decoders.BASE64URL.decode(SECURITY_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generatedToken(User user)
	{
		return Jwts
				.builder()
				.setSubject(user.getEmail())
				.claim("role", user.getRole())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000))
				.signWith(getSigninKey())
				.compact();
	}
	
	public String extractUserName(String token)
	{
		return extractClaim(token, Claims::getSubject);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> resolver) {
		// TODO Auto-generated method stub
		Claims claims=extractAllClaims(token);
		return resolver.apply(claims);
	}

	public boolean isTokenExpired(String token)
	{
		return extactExpiration(token).before(new Date());
	}

	private Date extactExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getExpiration);
	}
	
	public boolean isValid(String token,UserDetails user)
	{
		String userName=extractUserName(token);
		boolean validToken=tokenRepo.findByToken(token).map(t -> !t.isLogout()).orElse(false);
		return (userName.contains(user.getUsername()) && !isTokenExpired(token) && validToken);
	}
	
	public String extractUserRole(String token)
	{
		return extractClaim(token, claims -> claims.get("role",String.class) );
	}
	
	
	
}
