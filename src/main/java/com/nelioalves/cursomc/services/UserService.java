package com.nelioalves.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nelioalves.cursomc.security.UserSS;

public class UserService {
	// retorna o usuario logado
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		} catch (Exception e) {
			return null;
		}
	}
}
