package com.nelioalves.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nelioalves.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String email;
	private String senha;
	// lista para ficar igual o que o spring security exige
	Collection<? extends GrantedAuthority> authorities;

	public UserSS() {

	}

	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		//convertendo lista de perfils para authorities
		this.authorities = perfis.stream()
				.map(x -> new SimpleGrantedAuthority(x.getDescricao()))
				.collect(Collectors.toList());
	}

	public Integer getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// verifica se a conta não está expirada
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// conta não está bloqueado
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// credenciais não estão expirada
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Usuario está ativo
		return true;
	}
//classe de usuario do spring security

}
