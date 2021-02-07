package com.nelioalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exception.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	//gerar numeros aleatórios
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		//cria uma senha aleatória
		String newPass = newPassword();
		//seta o cliente para a nova senha
		cliente.setSenha(pe.encode(newPass));
		//salva a nova senha
		clienteRepository.save(cliente);
		
		//envia uma nova senha
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i =0; i <10; i++) {
			vet[i] = randomChar();
		}
		
		return new String(vet);
	}

	//gera senha tanto letra maiuscula, minuscula e numero
	private char randomChar() {
		int opt =  rand.nextInt(3);
		
		if(opt == 0) {//gera um digito
		   return(char) (rand.nextInt(10) + 48);	
		}else if(opt == 1){//gera letra maiuscula
			return(char) (rand.nextInt(26) + 65);		
		}else {//gera letra minuscula
			return(char) (rand.nextInt(26) + 97);
		}
	}
	
	

}
