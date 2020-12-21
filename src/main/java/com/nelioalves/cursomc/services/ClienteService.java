package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exception.DateIntegrityException;
import com.nelioalves.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired //automaticamente é instanciada
   private ClienteRepository repo;
	
   public Cliente find(Integer id) {
	   Optional<Cliente> obj = repo.findById(id);
	   return obj.orElseThrow(() -> new ObjectNotFoundException(
			   "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName())); 
   }
   
   
   public Cliente insert(Cliente obj) {
	   obj.setId(null);
	   return repo.save(obj);
   }
   
   public Cliente update(Cliente obj) {
	   Cliente newObj = find(obj.getId());
	   //atualiza de acordo com as informações do bd
	   updateData(newObj, obj);
	   return repo.save(newObj);
   }
   
   public void delete(Integer id) {
	   find(id);
	   try {
	      repo.deleteById(id);
	   }catch(DataIntegrityViolationException e) {
		   throw new DateIntegrityException("Não é Possível excluir porque a entidades relacionadas");   
	   }
   }
   
   public List<Cliente> findAll(){
	   return repo.findAll();   
   }
   
   public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
	   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),
			   orderBy); 
	   
	   return repo.findAll(pageRequest);
   }
   
   //metodo auxiliar Categoria para CategoriaDTO
   
   public Cliente fromDTO(ClienteDTO objDto) {
	   return new Cliente(objDto.getId(),
			   objDto.getNome(),
			   objDto.getEmail(),
			   null,
			   null);
   }
   
   private void updateData(Cliente newObj, Cliente Obj) {
	   newObj.setNome(Obj.getNome());
	   newObj.setEmail(Obj.getEmail());
	   
   }
}
