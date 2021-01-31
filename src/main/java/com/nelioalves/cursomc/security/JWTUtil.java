package com.nelioalves.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
