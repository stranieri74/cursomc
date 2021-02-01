package com.nelioalves.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil =jwtUtil;
		//para realizar a busca do email do usuario
		this.userDetailsService = userDetailsService;
	}
	
	//metodo que realiza a validação, antes de abrir o sistema
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			                        HttpServletResponse response,
			                        FilterChain chain) throws IOException, ServletException{
	//guardando o valor da autorização
		String header = request.getHeader("Authorization");
		System.out.println("entrei aqui 1 " +header);
		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			System.out.println("entrei aqui 2 "+auth);
			if(auth != null) {
				//libera o usuario para acessar o endPoint
				SecurityContextHolder.getContext().setAuthentication(auth);
				System.out.println("entrei aqui 3 ");
			}
		}
		//chamo o metodo que pode continuar fazendo a requisição
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
		}
		return null;
	}

}
