package com.nelioalves.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	//pegando os valores do property
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	//gerando o token
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username) //usuario de autenticação
				.setExpiration(new Date(System.currentTimeMillis() + expiration)) // tempo de expiração
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()) //aqui falo o algoritmo de autenticação
	            .compact();//para finalizar
	}
	
	public boolean tokenValido(String token) {
		//função que revindica o token
		Claims claims = getClaims(token);
		if (claims != null){
		   //pega o usuario do clains
			String username = claims.getSubject();
			//para validar se o token está expirado
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			
			if (username != null && expirationDate !=null
					&& now.before(expirationDate)) {
			   return true;	
			}
		   
		}
		return false;
	}
	
	
	//obtem os Cleams através do token
	private Claims getClaims(String token) {
		try {
		return Jwts.parser().setSigningKey(secret.getBytes())
				.parseClaimsJws(token).getBody();
		}catch(Exception e) {
			return null;
		}
	
	}
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null){
		   //pega o usuario do clains
			return claims.getSubject();
		}
		return null;
	}
}
