package com.nelioalves.cursomc.recources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos") 
public class PedidoResource {
	@Autowired
	private PedidoService service;
	
	 @RequestMapping(value="/{id}", method=RequestMethod.GET)
   public
   ResponseEntity<Pedido> find(@PathVariable Integer id) {
	//ResponseEntity<?> find(@PathVariable Integer id) { 
		 Pedido obj = service.find(id);
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
