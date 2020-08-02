package com.nelioalves.cursomc.recources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes") 
public class ClienteResource {
	@Autowired
	private ClienteService service;
	
	 @RequestMapping(value="/{id}", method=RequestMethod.GET)
   public
   ResponseEntity<?> find(@PathVariable Integer id) {
	 
	Cliente obj = service.buscar(id);
	 return ResponseEntity.ok().body(obj);  
   }
	 
/*public List<Categoria> listar() {
	  Categoria cat1 = new Categoria(1, "Informatica");
	  Categoria cat2 = new Categoria(2, "Escritório"); 
	  List<Categoria> lista= new ArrayList<>();
	  lista.add(cat1);
	  lista.add(cat2);
	  return lista;   
   }*/
}
