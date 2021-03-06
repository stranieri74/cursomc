package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.SmtpEmailServices;

@Configuration
@Profile("dev")

public class DevConfig {


	
	@Autowired
	private DBService dbService;
	
	//validar se o valor é create 
	@Value("${spring.jpa.hibernate.ddl-auto}")
	
	private String strategy;	
	@Bean
	//instancia o banco de dados
	public boolean instantiateDataBase() throws ParseException {
		//validar se o valor é create 
		if(!"create".equals(strategy)) {
			return false;
					
		}
	   dbService.instantiateTestDataBase();
		
		return true;
	}
	
	//fica disponivel como um componente quando declaro @Bean
		@Bean
		public EmailService emailService() {
			return new SmtpEmailServices();
		}
}
