package com.nelioalves.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nelioalves.cursomc.security.JWTAuthenticationFilter;
import com.nelioalves.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
			};
	
	private static final String[] PUBLIC_MATCHERS_GET = { 
			"/produtos/**", 
			"/categorias/**",
			"/clientes/**"
			};

	// rescrevendo o metodo HttpSecuryt
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			//libera o acesso ao banco h2
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();
       http.authorizeRequests()
       .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
       .antMatchers(PUBLIC_MATCHERS).permitAll()
       .anyRequest().authenticated();
       //adicionando o filtro
       http.addFilter(new JWTAuthenticationFilter(authenticationManager(),
    		   jwtUtil));
       //não utiliza sessão
       http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	//sobrescrevendo o configure de autenticação falando qual é o modo de ccriptografia da senha
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		//o Spring busca automaticamente uma assinatura para instanciar o userDetaisService
		//utilizando a userDetailsServiceImp que escrevemos
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	//acesso basico de multiplas fontes
	//permitindo acesso nos andpoints
	@Bean
	CorsConfigurationSource CorsCOnfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	//apenas para criptografar a senha
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
